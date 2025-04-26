package org.tfidf;

import org.tfidf.crawler.WebCrawler;
import org.tfidf.index.InvertedIndex;
import org.tfidf.ranking.Ranker;
import org.tfidf.ranking.TFIDFCalculator;

import java.util.List;
import java.util.Scanner;

public class Main {
        public static void main(String[] args) throws Exception {
                final int MAX_PAGES = 10;

                // WebCrawler crawler = new WebCrawler();
                //
                // System.out.println("Seed 1");
                // crawler.crawl("https://en.wikipedia.org/wiki/Pharaoh", MAX_PAGES);
                //
                // System.out.println("\nSeed 2");
                // crawler.crawl("https://en.wikipedia.org/wiki/List_of_pharaohs", MAX_PAGES);

                ///////////////////////////////////////////////////////////////////////

                // 1. Start crawling from seed URLs
                List<String> documents = WebCrawler.crawlToMemory("https://en.wikipedia.org/wiki/Pharaoh", MAX_PAGES);
                documents.addAll(WebCrawler.crawlToMemory("https://en.wikipedia.org/wiki/List_of_pharaohs", MAX_PAGES));

                // System.out.println(documents);
                System.out.println("Crawled " + documents.size() + " documents");

                // documents.forEach(
                // doc ->
                // {
                // System.out.println(doc);
                // System.out.println("\n\n");
                // }
                // );

                // 2. build inverted index
                InvertedIndex index = new InvertedIndex();
                System.out.println("Loading....");

                index.buildIndex(documents);
                System.out.println("Finish indexing");

                // index.printIndex();

                // 3. Compute TF-IDF weights
                TFIDFCalculator tfidfCalculator = new TFIDFCalculator(index, documents.size());
                tfidfCalculator.compute(); // prepares TF-IDF weights for all terms.

                Ranker ranker = new Ranker(index, tfidfCalculator);

                Scanner scanner = new Scanner(System.in);
                while (true) {// // 4. Get user query
                        System.out.println("Enter exit() or your search query:");
                        String queryInput = scanner.nextLine();
                        if (queryInput.equals("exit()"))
                                break;

                        // // 5. Rank documents by cosine similarity
                        ranker.rankAndDisplayTopDocuments(queryInput, 10); // processes the query, computes cosine
                                                                           // similarity, and prints top-k documents.
                }
                scanner.close();
        }
}