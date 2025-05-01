package org.tfidf.index;

public class DocumentInfo {
    public final String url;
    public final String title;

    DocumentInfo(String url, String title) {
        this.url = url;
        this.title = title;
    }

    @Override
    public String toString() {
        return "URL: " + url + ", Title: " + title;
    }
}