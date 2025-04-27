package org.tfidf.ranking;

import org.tfidf.index.InvertedIndex;
import org.tfidf.index.Posting;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class Ranker {
    private final InvertedIndex index;
    private final TFIDFCalculator tfidfCalculator;
    public Ranker(InvertedIndex index, TFIDFCalculator tfidfCalculator) {
        this.index = index;
        this.tfidfCalculator = tfidfCalculator;
    }

    public void rankAndDisplayTopDocuments(String query, int topK) {
        List<DocumentTFIDF> queryVector = computeQueryVector(query);
        List<DocumentTFIDF> rankedDocuments = new ArrayList<>();

        for (Integer term : queryVector.stream().map(DocumentTFIDF::getDocId).collect(Collectors.toSet())) {
            List<DocumentTFIDF> docVector = tfidfCalculator.GetDocumentVectors().get(term);
            if (docVector != null) {
                double similarity = CosineSimilarity.calculate(queryVector, docVector);
                rankedDocuments.add(new DocumentTFIDF(term, similarity)); // Assuming DocumentTFIDF can take a similarity score
            }
        }

        // Sort documents by similarity score
        rankedDocuments.sort((d1, d2) -> Double.compare(d2.getTfidf(), d1.getTfidf()));

        // Display top K documents
        for (int i = 0; i < Math.min(topK, rankedDocuments.size()); i++) {
            System.out.println("Document ID: " + rankedDocuments.get(i).getDocId() + " - Similarity: " + rankedDocuments.get(i).getTfidf());
        }
    }

    private List<DocumentTFIDF> computeQueryVector(String query) {
        List<DocumentTFIDF> queryVector = new ArrayList<>();
        String[] terms = query.split("\\s+");

        for (String term : terms) {
            for (String t : index.getIndex().keySet()) {
                if (Objects.equals(term, t)) {
                    System.out.println(term);
                    List<Posting> postings = index.getIndex().get(term);
                    double idf = Math.log10((double) tfidfCalculator.calculateIDF(term) / postings.size());
                    for (Posting p : postings) {
                        double tfWeight = p.tf;
                        double tfidf = tfWeight * idf;
                        queryVector.add(new DocumentTFIDF(p.docID, tfidf)); // Using hashCode as a temporary docId
                    }
                }

            }
        }
        return queryVector;
    }
}
