package org.tfidf.ranking;

class DocumentTFIDF {
    public final int docId;
    public final double tfidf;

    public DocumentTFIDF(int docId, double tfidf) {
        this.docId = docId;
        this.tfidf = tfidf;
    }

    @Override
    public String toString() {
        return "(docId = " + docId + ", tfidf = " + tfidf + ')';
    }
}