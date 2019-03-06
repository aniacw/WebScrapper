package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
    @FXML
    TextField urlTextField;
    @FXML
    ListView<String> resultsListView;
    @FXML
    Label statusBar;

    LinkFinder linkFinder;

    public void initialize(){
        linkFinder=new LinkFinder();
        linkFinder.setLinksLimit(10);
    }

    private static String downloadPage(String address) throws IOException {
        URL url=new URL(address);
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder(500000);
        String line = reader.readLine();
        while (line != null){
            builder.append(line);
            builder.append('\n');
            line=reader.readLine();
        }
        return builder.toString();
    }

    public void runButtonClicked(ActionEvent actionEvent) {
        resultsListView.getItems().clear();
        linkFinder.clear();
        linkFinder.add(urlTextField.getText());
        resultsListView.getItems().add("TEST");
        String url;
        do {
            url = linkFinder.next();
            System.out.println(url);
            //resultsListView.getItems().add(url);
            //resultsListView.refresh();
            try {
                long t0 = System.currentTimeMillis();
                String pageContent = downloadPage(url);
                long t1 = System.currentTimeMillis() - t0;
                statusBar.setText("Time: " + t1 + "ms, Length: " + pageContent.length());
                linkFinder.search(pageContent);
            }
            catch (MalformedURLException e){
                statusBar.setText("Invalid URL");
                resultsListView.getItems().add("[INVALID] " + url);
            }
            catch (IOException e){
                statusBar.setText("Connection failed");
                resultsListView.getItems().add("[FAILED] " + url);
            }
        } while (url != null);

    }
}
