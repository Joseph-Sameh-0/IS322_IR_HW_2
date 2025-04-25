package org.tfidf.index;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import org.tfidf.text.Normalizer;

public class InvertedIndex {
    private final Map<String, List<Posting>> index = new HashMap<>();
    private final Normalizer normalizer = new Normalizer();

    // Builds the inverted index from all .txt documents in the given folder
    public void buildIndex(String folderPath) throws Exception {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return;

        files = Arrays.stream(files)
                .sorted(Comparator.comparingInt(f -> extractFileNumber(f.getName())))
                .toArray(File[]::new);

        int docID = 1; 
        for (File file : files) {
            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) { // Read file content, skipping metadata lines like URL: and Title:
                String line;
                boolean skipHeader = true;
                while ((line = br.readLine()) != null) {
                    if (skipHeader && (line.startsWith("URL:") || line.startsWith("Title:") || line.trim().isEmpty())) {
                        continue;
                    }
                    skipHeader = false; 
                    contentBuilder.append(line).append(" ");
                }
            }

            Map<String, Integer> termFrequency = new HashMap<>();
            List<String> tokens = normalizer.getLemmas(contentBuilder.toString());
            for (String token : tokens) {
                token = token.toLowerCase().replaceAll("\\W+", ""); // Clean and normalize token: lowercase, remove non-word characters
                if (token.isEmpty()) continue;
                termFrequency.put(token, termFrequency.getOrDefault(token, 0) + 1); //count frequency
            }

            for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
                String term = entry.getKey();
                int tf = entry.getValue();
                index.computeIfAbsent(term, k -> new ArrayList<>()).add(new Posting(docID, tf)); //create the posting
            }

            docID++; 
        }
    }

    private int extractFileNumber(String name) {
        // Remove non-digits, parse as int
        String numStr = name.replaceAll("\\D+", "");
        return numStr.isEmpty() ? 0 : Integer.parseInt(numStr);
    }

    public Map<String, List<Posting>> getIndex() {
        return index;
    }

    // Print the inverted index 
    public void printIndex() {
        for (Map.Entry<String, List<Posting>> entry : index.entrySet()) {
            System.out.printf("%-15s -> %s%n", entry.getKey(), entry.getValue());
        }
    }
}
