package org.tfidf.ranking;


import java.util.Map;

public class CosineSimilarity {
    // calculate only dot products of two normalized vectors
    public static double calculate(Map<String, Double> vectorA, Map<String, Double> vectorB){
        double dotProduct = 0.0;

        // each vector contains terms and their normalized values(from 0 to 1) => 0 denotes not in document vector
        for (Map.Entry<String, Double> entryA : vectorA.entrySet()) {
            String term = entryA.getKey();
            double weightA = entryA.getValue();
            // in case term is found in vectorB get its value then
            double weightB = vectorB.getOrDefault(term, 0.0);

            dotProduct += weightA * weightB;
        }

        return dotProduct;
    }
}
