package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Controller {

    @FXML
    TextField urlTextField;

    @FXML
    ListView<String> phrasesListView;

    @FXML
    ListView<LinkResult> resultsListView;

    @FXML
    Label statusBar;

    @FXML
    Button searchPhraseButton;

    @FXML
    TextField linkCountTextField;

    LinkFinder linkFinder;
    PhraseFinder phraseFinder;
    ObservableList<LinkResult> results;

    public void initialize() {
        results=FXCollections.observableArrayList();
        linkFinder = new LinkFinder();
        linkFinder.setLinksLimit(10);
        phraseFinder = new PhraseFinder();
        phrasesListView.setCellFactory(TextFieldListCell.forListView());
        for (int i=0;i<10;++i)
            phrasesListView.getItems().add("");
    }

    private static String downloadPage(String address) throws IOException {
        URL url = new URL(address);
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder(500000);
        String line = reader.readLine();
        while (line != null) {
            builder.append(line);
            builder.append('\n');
            line = reader.readLine();
        }
        return builder.toString();
    }

    private void addLinkToResults(String link, int coef){
        results.add(new LinkResult(link, coef));
        SortedList<LinkResult> sortedLinks = new SortedList<>(results);
        sortedLinks.setComparator((l1, l2) -> l2.coef-l1.coef);

//        sortedLinks.sort(new Comparator<LinkResult>() {
//            @Override
//            public int compare(LinkResult l1, LinkResult l2) {
//                return l2.coef - l1.coef;
//            }
//        });

        resultsListView.setItems(sortedLinks);
    }

    public void runButtonClicked(ActionEvent actionEvent) {
        setLinkLimit();
        phraseFinder.setPhrases(phrasesListView.getItems());
        results.clear();
        resultsListView.setItems(results);
        linkFinder.clear();
        linkFinder.add(urlTextField.getText());

        String url;
        url = linkFinder.next();
        while (url != null) {
           // System.out.println(url);
            //resultsListView.refresh();
            try {
                long t0 = System.currentTimeMillis();
                String pageContent = downloadPage(url);
                long t1 = System.currentTimeMillis() - t0;
                int coef = phraseFinder.computeCoefficient(pageContent);
                addLinkToResults(url, coef);

                statusBar.setText("Time: " + t1 + "ms, Length: " + pageContent.length());
                linkFinder.search(pageContent);
            } catch (MalformedURLException e) {
                statusBar.setText("Invalid URL");
                addLinkToResults("[INVALID] " + url, 0);
            } catch (IOException e) {
                statusBar.setText("Connection failed");
                addLinkToResults("[FAILED] " + url, 0);
            }
            url  =linkFinder.next();
        }

    }

    private void setLinkLimit() {
        String linkCountStr = linkCountTextField.getText();
        try {
            linkFinder.setLinksLimit(Integer.parseInt(linkCountStr));
        }
        catch (NumberFormatException e){
            linkFinder.setLinksLimit(10);
        }
    }

    public void searchPhraseButtonClicked() {
        resultsListView.getItems().clear();
        phraseFinder.clear();

    }
}
