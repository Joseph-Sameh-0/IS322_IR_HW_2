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
            double idf = calculateIDF(term);

            for (Posting posting : postings) {
                int docId = posting.docID;
                int tf = posting.tf;

                double tfWeight = 1 + Math.log10(tf); // log frequency weighting

                double tfidf = tfWeight * idf;

                DocumentTFIDF docTFIDF = new DocumentTFIDF(docId, tfidf);
                documentVectors.computeIfAbsent(term, k -> new ArrayList<>()).add(docTFIDF);
            }
        }
    }

    public double calculateIDF(String term) {
        if (!invertedIndex.containsKey(term)) {
            return 0;
        }
        int df = invertedIndex.get(term).size();
        return Math.log10((double) totalDocuments / df);
    }

    public Map<String, List<DocumentTFIDF>> GetDocumentVectors() {
        return documentVectors;
    }

    public double getTFIDF(String term, int docId) {
        List<DocumentTFIDF> docs = documentVectors.get(term);
        if (docs == null)
            return 0;

        for (DocumentTFIDF doc : docs) {
            if (doc.docId == docId) {
                return doc.tfidf;
            }
        }
        return 0;
    }

    public void printIndex() {
        for (Map.Entry<String, List<DocumentTFIDF>> entry : documentVectors.entrySet()) {
            System.out.printf("%-1s -> %s%n", entry.getKey(), entry.getValue());
        }
    }

    // Returns the TF-IDF vector for a specific document
    public Map<String, Double> getDocumentVector(int docId) {
        Map<String, Double> vector = new HashMap<>();

        for (Map.Entry<String, List<DocumentTFIDF>> entry : documentVectors.entrySet()) {
            String term = entry.getKey();
            List<DocumentTFIDF> docList = entry.getValue();

            for (DocumentTFIDF docTFIDF : docList) {
                if (docTFIDF.docId == docId) {
                    vector.put(term, docTFIDF.tfidf);
                }
            }
        }
        return vector;
    }

    // Returns the IDF values for all terms
    public Map<String, Double> getIdfValues() {
        Map<String, Double> idfMap = new HashMap<>();

        for (String term : invertedIndex.keySet()) {
            double idf = calculateIDF(term);
            idfMap.put(term, idf);
        }
        return idfMap;
    }
    
}
