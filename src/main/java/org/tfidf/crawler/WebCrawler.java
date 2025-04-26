package org.tfidf.crawler;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple Wikipedia Web Crawler.
 * Downloads articles starting from a seed URL, saves their content in Documents.
 */

public class WebCrawler {
    // Set to store all visited URLs to avoid revisiting and duplicating crawled links
    static HashSet<String> visited = new HashSet<>();
    // Counter used to uniquely name output files (e.g., doc1.txt, doc2.txt)
    static int docCounter = 1;
    /**
     * Crawl and save up to maxPages documents starting from the given seed URL.
     * Each visited document will be saved into a file under the Documents/ folder.
     */
    public static void crawl(String seedUrl, int maxPages) throws Exception {
        LinkedList<String> queue = new LinkedList<>();   // Queue to manage URLs to visit during crawling
        queue.add(seedUrl);
        int localSaved = 1;// track number of saved pages from this seed

        while (!queue.isEmpty() && localSaved <= maxPages) {
            String currentUrl = queue.poll();
             // Skip if already visited or invalid Wikipedia page
            if (visited.contains(currentUrl) || !currentUrl.startsWith("https://en.wikipedia.org/wiki/")) {
                continue;
            }
            visited.add(currentUrl);// mark as visited
            Document doc = Jsoup.connect(currentUrl).get();
            // Get the page title
            String title = doc.title();

            // Extract and clean paragraph text
            Elements paragraphs = doc.select("p");
            StringBuilder cleanText = new StringBuilder();
            for (Element p : paragraphs) {
                String paragraph = p.text();
                // Remove citation-like references such as [1], [2], [note 3], etc.
                paragraph = paragraph.replaceAll("\\[.*?\\]", "");
                if (!paragraph.trim().isEmpty()) {
                    cleanText.append(paragraph).append("\n");
                }
            }
             // Skip if page content is empty
            if (cleanText.toString().trim().isEmpty()) continue;
            
             // Create output folder if not already created
            new java.io.File("Documents").mkdirs();
            String filename = "Documents/doc" + docCounter + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("URL: " + currentUrl);
                writer.println("Title: " + title);
                writer.println();
                writer.println(cleanText);
            }
            docCounter++;
            localSaved++;
            System.out.println("Visited (" + visited.size() + "): " + currentUrl);
             // Extract links from current page and add valid ones to the queue
            for (Element link : doc.select("a[href]")) {
                String next_link = link.attr("abs:href").split("#")[0];
                if (isValidLink(next_link, queue)) {
                    queue.add(next_link);
                }
            }
        }
    }
    // Crawl pages and return document contents in memory (no file writing).
    // to process data in memory, for indexing
    public static List<String> crawlToMemory(String seedUrl, int maxPages) throws Exception {
        LinkedList<String> queue = new LinkedList<>();
        queue.add(seedUrl);
        List<String> documents = new ArrayList<>();
        int pagesProcessed = 0;

        while (!queue.isEmpty() && pagesProcessed < maxPages) {
            String currentUrl = queue.poll();

            // Skip if already visited or doesn't match our domain
            if (visited.contains(currentUrl) || !currentUrl.startsWith("https://en.wikipedia.org/wiki/")) {
                continue;
            }

            visited.add(currentUrl);
            System.out.println("Processing: " + currentUrl);

            try {
                Document doc = Jsoup.connect(currentUrl).get();
                String title = doc.title();

                // Extract and clean content
                Elements paragraphs = doc.select("p");
                StringBuilder content = new StringBuilder();
                for (Element p : paragraphs) {
                    String text = p.text().replaceAll("\\[.*?\\]", "");
                    if (!text.trim().isEmpty()) {
                        content.append(text).append("\n");
                    }
                }

                if (content.length() > 0) {
                    String document = "URL: " + currentUrl + "\n" +
                            "Title: " + title + "\n\n" +
                            content.toString();
                    documents.add(document);
                    pagesProcessed++;
                    System.out.println("Crawled (" + pagesProcessed + "): " + currentUrl);
                }

                // Extract and filter links
                for (Element link : doc.select("a[href]")) {
                    String nextUrl = link.attr("abs:href").split("#")[0];

                    if (isValidLink(nextUrl, queue)) {
                        if (!visited.contains(nextUrl) && !queue.contains(nextUrl)) {
                            queue.add(nextUrl);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing " + currentUrl + ": " + e.getMessage());
            }
        }
        return documents;
    }
 /**
     * Filter whether a given URL is valid for crawling.
     * Must be an English Wikipedia article (no special pages like "Category,Home").
     * Must contain at least one relevant keyword to stay on topic.
     */
    private static boolean isValidLink(String url, LinkedList<String> queue) {
        return url.startsWith("https://en.wikipedia.org/wiki/")
                && url.matches("https://en\\.wikipedia\\.org/wiki/[^:#]+")
                && !url.contains(":") && url.contains("Pharaoh")
                || url.contains("Ancient")
                || url.contains("Royal")
                || url.contains("First Dynasty")
                || url.contains("Kings")
                || url.contains("Old Kingdom")
                || url.contains("Amun")
                || url.contains("Akhenaten")
                || url.contains("Egypt")
                || url.contains("Dynasty")
                || url.contains("Roman") &&
                !visited.contains(url) && !queue.contains(url);
    }

}
