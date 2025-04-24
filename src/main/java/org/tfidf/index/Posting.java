package org.tfidf.index;

public class Posting {
    public int docID;
    public int tf;

    public Posting(int docID, int tf) {
        this.docID = docID;
        this.tf = tf;
    }

    @Override
    public String toString() {
        return "(docID=" + docID + ", tf=" + tf + ")";
    }
}
