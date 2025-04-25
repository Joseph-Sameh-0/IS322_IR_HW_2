package org.tfidf;

import org.tfidf.crawler.WebCrawler;
import org.tfidf.index.InvertedIndex;
import org.tfidf.ranking.Ranker;
import org.tfidf.ranking.TFIDFCalculator;
import org.tfidf.text.TextProcessor;

import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
         final int MAX_PAGES = 10;

//         WebCrawler crawler = new WebCrawler();
//
//         System.out.println("Seed 1");
//         crawler.crawl("https://en.wikipedia.org/wiki/Pharaoh", MAX_PAGES);
//
//         System.out.println("\nSeed 2");
//         crawler.crawl("https://en.wikipedia.org/wiki/List_of_pharaohs", MAX_PAGES);

//        InvertedIndex index = new InvertedIndex();
//        index.buildIndex("Documents");
//        index.printIndex();




        ////////////////////////////////

        // 1. Start crawling from seed URLs
        List<String> documents = WebCrawler.crawlToMemory("https://en.wikipedia.org/wiki/Pharaoh", MAX_PAGES);

        System.out.println(documents);
//        // 2. build inverted index
//        InvertedIndex index = new InvertedIndex();
//        index.buildIndex(documents);
//
//        // 3. Compute TF-IDF weights
//        TFIDFCalculator tfidfCalculator = new TFIDFCalculator(index);
//        tfidfCalculator.compute(); //prepares TF-IDF weights for all terms.
//
//        // 4. Get user query
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter your search query:");
//        String queryInput = scanner.nextLine();
//
//        // 5. Rank documents by cosine similarity
//        Ranker ranker = new Ranker(index, tfidfCalculator);
//        ranker.rankAndDisplayTopDocuments(queryInput, 10); //processes the query, computes cosine similarity, and prints top-k documents.


    }
}