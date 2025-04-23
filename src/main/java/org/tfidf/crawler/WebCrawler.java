package org.tfidf.crawler;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;


public class WebCrawler {
    static HashSet<String> visited = new HashSet<>();
    static int docCounter = 1;
    public static void main(String[] args) throws Exception {
        final int MAX_PAGES = 10;

//        List<String> keywordsSeed1 = List.of("Pharaoh", "Ancient", "Old_Kingdom", "Amun", "Akhenaten", "Egypt", "Dynasty", "Pyramid");
//        List<String> keywordsSeed2 = List.of("Dynasty", "List_of_pharaohs", "Royal", "Horus", "Nomen", "Scepter", "Chronology", "Kings");

        System.out.println("Seed 1:");
        crawl("https://en.wikipedia.org/wiki/Pharaoh", MAX_PAGES);

        System.out.println("\nSeed 2:");
        crawl("https://en.wikipedia.org/wiki/List_of_pharaohs", MAX_PAGES);
    }
    public static void crawl(String seedUrl, int maxPages) throws Exception {
        LinkedList<String> queue = new LinkedList<>();
        queue.add(seedUrl);
        int  localSaved = 1;

        while (!queue.isEmpty() && localSaved <= maxPages) {
            String currentUrl = queue.poll();
            if (visited.contains(currentUrl)|| !currentUrl.startsWith("https://en.wikipedia.org/wiki/")) {
                continue;
            }
                visited.add(currentUrl);
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

            if (cleanText.toString().trim().isEmpty()) continue;
            String filename = "doc" + docCounter + ".txt";
                        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                            writer.println("URL: " + currentUrl);
                            writer.println("Title: " + title);
                            writer.println();
                            writer.println(cleanText);
                        }
                 docCounter++;
                 localSaved++;
                System.out.println("Visited (" + visited.size() + "): " + currentUrl);
                for (Element link : doc.select("a[href]")) {
                    String next_link = link.attr("abs:href").split("#")[0];
                    if (next_link.startsWith("https://en.wikipedia.org/wiki/") && next_link.matches("https://en\\.wikipedia\\.org/wiki/[^:#]+") &&!next_link.contains(":") && next_link.contains("Pharaoh")||next_link.contains("Ancient")||next_link.contains("Royal")||next_link.contains("First Dynasty")||next_link.contains("Kings")||next_link.contains("Old Kingdom")||next_link.contains("Amun")||next_link.contains("Akhenaten")||next_link.contains("Egypt")||next_link.contains("Dynasty")||next_link.contains("Roman") &&
                        !visited.contains(next_link) && !queue.contains(next_link)) {
                        queue.add(next_link);
                    }
            }
        }
    }
}
