package org.tfidf.ranking;

import org.tfidf.index.DocumentInfo;
import org.tfidf.index.InvertedIndex;
import org.tfidf.text.Normalizer;

import java.util.*;
import java.util.stream.Collectors;

public class Ranker {
    private final TFIDFCalculator tfidfCalculator;
    private final InvertedIndex index;
    private final Normalizer normalizer = new Normalizer();

    public Ranker(InvertedIndex index, TFIDFCalculator tfidfCalculator) {
        this.tfidfCalculator = tfidfCalculator;
        this.index = index;
    }

    public void rankAndDisplayTopDocuments(String query, int topK) {
        // stem query terms
        List<String>queryTerms = normalizer.getLemmas(query);
        // Build query vector (unnormalized)
        // maps terms to their tf idf
        Map<String, Double> queryVector = new HashMap<>();
        for (String term : queryTerms) {
            double count = Collections.frequency(queryTerms, term);
            queryVector.put(term, count * tfidfCalculator.calculateIDF(term));// maps each term to its term frequency in query
        }
        queryVector = normalizeVector(queryVector);

        Map<Integer, Map<String, Double>> documentVectors = buildNormalizedDocumentVectors();

        Map<Integer, Double> scores = new HashMap<>();

        for (Map.Entry<Integer, Map<String, Double>> docEntry : documentVectors.entrySet()) {
            int docId = docEntry.getKey();
            Map<String, Double> docVector = docEntry.getValue();

            double similarity = CosineSimilarity.calculate(queryVector, docVector);
            scores.put(docId, similarity);
        }

        List<Map.Entry<Integer, Double>> filteredResults = scores.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)  // Filter out scores = 0
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(topK)
                .collect(Collectors.toList());

        if (filteredResults.isEmpty()) {
            System.out.println("No Result Found");
        } else {
            String divider = "+" + "-".repeat(45) + "+" + "-".repeat(14) + "+" + "-".repeat(102) + "+";

            System.out.println(divider);
            System.out.printf("| %-43s | %-12s | %-100s |%n", "Title", "Score", "URL");
            System.out.println(divider);

            scores.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                    .limit(topK)
                    .forEach(entry -> {
                        DocumentInfo docInfo = index.getDocumentInfo(entry.getKey());
                        double score = entry.getValue();

                        // Truncate title if too long
                        String title = docInfo.title.length() > 40 ? docInfo.title.substring(0, 37) + "..." : docInfo.title;

                        System.out.printf("| %-43s | %-1.10f | %-100s |%n", title, score, docInfo.url);
                    });
            System.out.println(divider);
        }
    }


    private Map<Integer, Map<String, Double>> buildNormalizedDocumentVectors() {
        Map<String, List<DocumentTFIDF>> rawVectors = tfidfCalculator.GetDocumentVectors();
        Map<Integer, Map<String, Double>> docVectors = new HashMap<>();

        // build raw tf-idf per document
        // for each term → list of (docId, tfidf) pairs
        for (Map.Entry<String, List<DocumentTFIDF>> entry : rawVectors.entrySet()) {
            String term = entry.getKey();
            // get all the documents where the term appears
            for (DocumentTFIDF doc : entry.getValue()) {
                // each document gets its own small map: term → tfidf
                docVectors.computeIfAbsent(doc.getDocId(), k -> new HashMap<>())
                        .put(term, doc.getTfidf());
            }
        }

        // Normalize each document vector
        Map<Integer, Map<String, Double>> normalizedVectors = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Double>> docEntry : docVectors.entrySet()) {
            normalizedVectors.put(docEntry.getKey(), normalizeVector(docEntry.getValue()));
        }

        return normalizedVectors;
    }


    // creates a new normalized vector by dividing each term's weight by the norm
    private Map<String, Double> normalizeVector(Map<String, Double> vector) {
        // sum all values (squared) and then square root the result => norm
        double norm = Math.sqrt(vector.values().stream().mapToDouble(val -> val * val).sum());
        if (norm == 0.0) {
            return vector; // Avoid division by zero
        }

        Map<String, Double> normalized = new HashMap<>();
        for (Map.Entry<String, Double> entry : vector.entrySet()) {
            normalized.put(entry.getKey(), entry.getValue() / norm);
        }

        return normalized;
    }
}
