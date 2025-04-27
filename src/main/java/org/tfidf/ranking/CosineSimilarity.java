package org.tfidf.ranking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CosineSimilarity {
    public static double calculate(List<DocumentTFIDF> vectorA, List<DocumentTFIDF> vectorB){
        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        // create a map for vectorB for easy access
        Map<Integer, Double> vectorBMap = new HashMap<>();
        for (DocumentTFIDF doc: vectorB){
            vectorBMap.put(doc.getDocId(), doc.getTfidf());
        }

        // calculate dot product and magnitudes
        for (DocumentTFIDF docA: vectorA){
            dotProduct += docA.getTfidf() * vectorBMap.getOrDefault(docA.getDocId(), 0.0);
            magnitudeA += Math.pow(docA.getTfidf(), 2);
        }

        for (DocumentTFIDF docB: vectorB){
            magnitudeB += Math.pow(docB.getTfidf(), 2);
        }
        if (magnitudeA == 0 || magnitudeB == 0) {
            return 0.0; // Avoid division by zero
        }

        return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));

    }
}
