package org.tfidf.text;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;

import java.util.Properties;
public class Normalizer {

    private StanfordCoreNLP pipeline;

    public Normalizer() {
        // Set up the properties for the pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        props.setProperty("outputFormat", "text");

        // Build the pipeline
        pipeline = new StanfordCoreNLP(props);
    }

    public void normalizeText(String text) {
        // Create a document object
        CoreDocument document = new CoreDocument(text);

        // Annotate the document
        pipeline.annotate(document);

        // Iterate through the tokens and print the original word and its lemma
        for (CoreLabel token : document.tokens()) {
            String word = token.word();
            String lemma = token.lemma();
            System.out.println("Original Word: " + word + " - Lemma: " + lemma);
        }
    }

    public static void main(String[] args) {
        Normalizer normalizer = new Normalizer();
        normalizer.normalizeText("ran");
    }

}
