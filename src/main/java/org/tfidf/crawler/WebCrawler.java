package org.tfidf.crawler;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WebCrawler {
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
        HashSet<String> visited = new HashSet<>();
        queue.add(seedUrl);
        while (!queue.isEmpty() && visited.size() < maxPages) {
            String currentUrl = queue.poll();
            if (visited.contains(currentUrl)) {
                continue;
            }
                visited.add(currentUrl);
                Document doc = Jsoup.connect(currentUrl).get();

                System.out.println("Visited (" + visited.size() + "): " + currentUrl);
                for (Element link : doc.select("a[href]")) {
                    String next_link = link.attr("abs:href").split("#")[0];
                    if (next_link.startsWith("https://en.wikipedia.org/wiki/") && !next_link.contains(":") && next_link.contains("Pharaoh")||next_link.contains("Ancient")||next_link.contains("Royal")||next_link.contains("First Dynasty")||next_link.contains("Kings")||next_link.contains("Old Kingdom")||next_link.contains("Amun")||next_link.contains("Akhenaten")||next_link.contains("Egypt")||next_link.contains("Dynasty")||next_link.contains("Roman") &&
                            !visited.contains(next_link) && !queue.contains(next_link)) {
                        queue.add(next_link);
                    }


            }
        }


    }
}
