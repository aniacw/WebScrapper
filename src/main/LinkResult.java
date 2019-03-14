package main;

public class LinkResult {
    public String link;
    public int coef;

    @Override
    public String toString(){
        return coef + ":  " + link;
    }

    public LinkResult(String link, int coef) {
        this.link = link;
        this.coef = coef;
    }
}
