package main;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkFinder {
    private HashSet<String> links;
    private LinkedList<String> queuedLinks;
    private int linksLimit;

    public int getLinksLimit() {
        return linksLimit;
    }

    public void setLinksLimit(int linksLimit) {
        this.linksLimit = linksLimit;
    }


//    private static final Pattern urlPattern =
//            Pattern.compile("<a href\\s*=\\s*\"(?:https://|http://)?([^\"]+)/?\"");
private static final Pattern urlPattern =
        Pattern.compile("<a href\\s*=\\s*\"([^\"]+)\"");

    public LinkFinder(){
        links=new HashSet<>();
        queuedLinks = new LinkedList<>();
        linksLimit = 10;
    }

    public void search(String text){
        Matcher matcher = urlPattern.matcher(text);
        while (matcher.find()){
            if (links.size() >= linksLimit)
                break;
            String url = matcher.group(1);
            if (links.add(url)){
                queuedLinks.add(url);
            }
        }
    }

    public String foundLinks(){
        StringBuilder builder=new StringBuilder();
        for (String s : links)
            builder.append(s).append('\n');
        return builder.toString();
    }

    public void add(String url){
        if (links.add(url))
            queuedLinks.add(url);
    }

    public String next(){
        return queuedLinks.poll();
    }

    public void clear(){
        links.clear();
        queuedLinks.clear();
    }
}
