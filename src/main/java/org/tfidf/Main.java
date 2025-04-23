package org.tfidf;
import org.tfidf.crawler.WebCrawler;

public class Main {
    public static void main(String[] args) throws Exception {
        final int MAX_PAGES = 10;

        WebCrawler crawler = new WebCrawler();

        System.out.println("Seed 1");
        crawler.crawl("https://en.wikipedia.org/wiki/Pharaoh", MAX_PAGES);

        System.out.println("\nSeed 2");
        crawler.crawl("https://en.wikipedia.org/wiki/List_of_pharaohs", MAX_PAGES);

    }
}