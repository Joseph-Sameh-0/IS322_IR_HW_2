package org.tfidf.index;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import org.tfidf.text.Normalizer;

public class InvertedIndex {
    private final Map<String, List<Posting>> index = new HashMap<>();
    private final Normalizer normalizer = new Normalizer();
    private final Map<Integer, DocumentInfo> documentInfoMap = new HashMap<>();

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
            String url = null;
            String title = null;
            StringBuilder contentBuilder = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("URL:")) {
                        url = line.substring(4).trim();
                        continue;
                    }
                    if (line.startsWith("Title:")) {
                        title = line.substring(6).trim();
                        continue;
                    }
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    contentBuilder.append(line).append(" ");
                }
            }

            // Store document metadata
            if (url != null && title != null) {
                documentInfoMap.put(docID, new DocumentInfo(url, title));
            }

            Map<String, Integer> termFrequency = new HashMap<>();
            List<String> tokens = normalizer.getLemmas(contentBuilder.toString());
            for (String token : tokens) {
                token = token.replaceAll("\\W+", "");
                if (token.isEmpty()) continue;
                termFrequency.put(token, termFrequency.getOrDefault(token, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
                String term = entry.getKey();
                int tf = entry.getValue();
                index.computeIfAbsent(term, k -> new ArrayList<>()).add(new Posting(docID, tf));
            }

            docID++;
        }
    }

    /**
     * Builds the inverted index from a list of documents
     * @param documents List of document contents (each String is one document)
     */
    public void buildIndex(List<String> documents) {
        if (documents == null || documents.isEmpty()) return;

        int docID = 1;
        for (String document : documents) {
            String url = null;
            String title = null;
            StringBuilder contentBuilder = new StringBuilder();

            String[] lines = document.split("\n");
            for (String line : lines) {
                if (line.startsWith("URL:")) {
                    url = line.substring(4).trim();
                    continue;
                }
                if (line.startsWith("Title:")) {
                    title = line.substring(6).trim();
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                contentBuilder.append(line).append(" ");
            }

            // Store document metadata
            if (url != null && title != null) {
                documentInfoMap.put(docID, new DocumentInfo(url, title));
            }

            Map<String, Integer> termFrequency = new HashMap<>();
            List<String> tokens = normalizer.getLemmas(contentBuilder.toString());

            for (String token : tokens) {
                token = token.replaceAll("\\W+", "");
                if (!token.isEmpty()) {
                    termFrequency.put(token, termFrequency.getOrDefault(token, 0) + 1);
                }
            }

            // Add to index
            for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
                String term = entry.getKey();
                int tf = entry.getValue();
                index.computeIfAbsent(term, k -> new ArrayList<>())
                        .add(new Posting(docID, tf));
            }

            docID++;
        }
    }

    private int extractFileNumber(String name) {
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

    public int getDocumentCount() {
        return documentInfoMap.size();
    }

    public Set<String> getTerms() {
        return index.keySet();
    }

    public int getDocumentFrequency(String term) {
        List<Posting> postings = index.get(term);
        return postings == null ? 0 : postings.size();
    }

    public Map<String, Integer> getTermFrequencies(int docId) {
        Map<String, Integer> termFreq = new HashMap<>();
        for (Map.Entry<String, List<Posting>> entry : index.entrySet()) {
            String term = entry.getKey();
            for (Posting posting : entry.getValue()) {
                if (posting.docID == docId) {
                    termFreq.put(term, posting.tf);
                }
            }
        }
        return termFreq;
    }

    public Set<Integer> getAllDocumentIds() {
        return documentInfoMap.keySet();
    }

    public int getTermTotalFrequency(String term) {
        List<Posting> postings = index.get(term);
        if (postings == null) {
            return 0;
        }
        int totalFrequency = 0;
        for (Posting posting : postings) {
            totalFrequency += posting.tf;
        }
        return totalFrequency;
    }

    public String getUrlByDocId(int docId) {
        DocumentInfo info = documentInfoMap.get(docId);
        return info != null ? info.url : null;
    }

    public String getTitleByDocId(int docId) {
        DocumentInfo info = documentInfoMap.get(docId);
        return info != null ? info.title : null;
    }

    public DocumentInfo getDocumentInfo(int docId) {
        return documentInfoMap.get(docId);
    }

}