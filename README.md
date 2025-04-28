# Java Web Crawler with TF-IDF and Cosine Similarity

## 🧠 Project Overview

This Java-based application performs focused web crawling starting from two Wikipedia seed URLs, builds an inverted index of the crawled pages, processes user queries using TF-IDF weighting, and ranks the documents based on cosine similarity to the query.

## 🎯 Main Features

- Crawls up to 10 distinct Wikipedia pages starting from two given URLs.
- Processes the text of each page via tokenization and normalization.
- Builds an inverted index mapping terms to their frequency in each document.
- Computes TF-IDF scores for documents and user queries.
- Calculates cosine similarity to rank and return the top 10 most relevant pages.

---

## 👨‍💻 Responsibilities

| Module | Responsibilities | Assigned Team Members |
|--------|------------------|------------------------|
| **Web Crawler Module** | - Crawl up to 10 distinct Wikipedia pages. <br> - Stay within Wikipedia domain and avoid duplicates. | Amany, Israa |
| **Text Processing & Inverted Index** | - Tokenize and normalize webpage text. <br> - Build an inverted index with term frequencies. | Joseph, Yousef |
| **TF-IDF & Ranking** | - Compute TF-IDF for all terms. <br> - Compute cosine similarity with user query. <br> - Rank and return top 10 documents. | Salma, Jonathan |
| **Documentation & Comments** | - Write inline code comments. <br> - Create a 2–3 page report and user manual. | Whole Team |
| **Presentation** | - Present each module and demo the full system. <br> - Ensure full team understanding of the codebase. | Whole Team |

---

## 🏗️ Maven Project Structure

```
webcrawler-tfidf/
├── pom.xml
├── README.md
├── report/
│   ├── Report.pdf
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── tfidf/
│                   ├── Main.java
│                   ├── crawler/
│                   │   └── WebCrawler.java            # Amany, Israa
│                   ├── text/
│                   │   └── Normalizer.java     # Joseph, Yousef
│                   ├── index/
│                   │   ├── InvertedIndex.java         # Joseph, Yousef
│                   │   └── Posting.java
│                   ├── ranking/
│                   │   ├── TFIDFCalculator.java       # Salma, Jonathan
│                   │   ├── CosineSimilarity.java
│                   │   └── Ranker.java
```

---

## 🚀 How to Run

1. Make sure you have Java 8+ and Maven installed.
2. Clone the repository and navigate into the project directory.
3. Add Jsoup dependency in `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.jsoup</groupId>
       <artifactId>jsoup</artifactId>
       <version>1.15.3</version>
   </dependency>
   ```
4. Build and run:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.tfidf.Main"
   ```
5. Follow the on-screen instructions to input your query and get ranked results.

