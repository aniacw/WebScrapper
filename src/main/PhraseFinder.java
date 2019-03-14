package main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhraseFinder {

    private List<String> phrases;
    private Pattern phrasesPattern;

    public PhraseFinder(){

    }

    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
        if (!phrases.isEmpty()){
            StringBuilder patternBuilder = new StringBuilder();
            patternBuilder.append(phrases.get(0));
            for (int i=1; i<phrases.size(); ++i) {
                String phrase = phrases.get(i);
                if (!phrase.isEmpty())
                    patternBuilder.append('|').append(phrase);
            }
            phrasesPattern = Pattern.compile(patternBuilder.toString());
        }
    }

    int computeCoefficient(String text){
        if (phrasesPattern == null)
            return 0;
        Matcher matcher = phrasesPattern.matcher(text);
        int count = 0;
        while (matcher.find()){
            ++count;
        }
        return count;
    }

    void clear(){
        phrases.clear();
        phrasesPattern = null;
    }
}
