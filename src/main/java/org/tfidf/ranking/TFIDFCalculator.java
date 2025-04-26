package org.tfidf.ranking;

import java.util.*;
import org.tfidf.index.Posting;

import org.tfidf.index.InvertedIndex;

public class TFIDFCalculator {

    private final Map<String, List<Posting>> invertedIndex;
    private final int totalDocuments;
    private Map<String, List<DocumentTFIDF>> documentVectors;

    public TFIDFCalculator(InvertedIndex invertedIndex, int totalDocuments) {
        this.invertedIndex = invertedIndex.getIndex();
        this.totalDocuments = totalDocuments;
        this.documentVectors = new HashMap<>();
    }

    public void compute() {
        for (String term : invertedIndex.keySet()) {
            List<Posting> postings = invertedIndex.get(term);
            double idf = calculateIDF(term); // Calculate IDF on demand

            for (Posting posting : postings) {
                int docId = posting.docID;
                int tf = posting.tf;

                // Calculate TF weight: 1 + log10(tf)
                double tfWeight = 1 + Math.log10(tf);

                // TF-IDF = TF * IDF
                double tfidf = tfWeight * idf;

                // Create DocumentTFIDF object
                DocumentTFIDF docTFIDF = new DocumentTFIDF(docId, tfidf);

                // Add to documentVectors
                documentVectors.computeIfAbsent(term, k -> new ArrayList<>()).add(docTFIDF);
            }
        }
    }

    private double calculateIDF(String term) {
        if (!invertedIndex.containsKey(term)) {
            return 0;
        }
        int df = invertedIndex.get(term).size(); // Document frequency
        return Math.log10((double) totalDocuments / df);
    }

    public void printIndex() {
        for (Map.Entry<String, List<DocumentTFIDF>> entry : documentVectors.entrySet()) {
            System.out.printf("%-1s -> %s%n", entry.getKey(), entry.getValue());
        }
    }
}
