package org.tfidf.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

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

    public List<String> getLemmas(String text) {
        List<String> lemmas = new ArrayList<>();
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        for (CoreLabel token : document.tokens()) {
            String lemma = token.lemma();
            if (lemma.matches("\\w+")) {
                lemmas.add(lemma.toLowerCase().replaceAll("\\W+", ""));
            }
        }
        return lemmas;
    }

//    public static void main(String[] args) {
//        Normalizer normalizer = new Normalizer();
//        normalizer.normalizeText("ran");
//    }
    
}
