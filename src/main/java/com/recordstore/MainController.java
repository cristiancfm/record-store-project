package com.recordstore;

import com.recordstore.model.Artist;
import com.recordstore.model.Item;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;

import javax.persistence.*;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final String APP_VERSION = "1.0";
    private final String GITHUB_LINK = "https://github.com/cristiancfm/record-store-project";

    @FXML
    private MenuItem menuExit;
    @FXML
    private MenuItem menuAbout;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //MENUS
        //exiting the application
        menuExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        //about window
        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert aboutWindow = new Alert(Alert.AlertType.INFORMATION);
                aboutWindow.setTitle("About Record Store");
                aboutWindow.setHeaderText("Record Store v."+ APP_VERSION);

                FlowPane flowPane = new FlowPane();
                Label label = new Label("""
                        This application can manage artists and their respective titles to be used in a record store.
                        Coded by Cristian Ferreiro Montoiro.
                        Icons from https://www.fatcow.com/free-icons""");

                Hyperlink githubLink = new Hyperlink(GITHUB_LINK);
                githubLink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            Desktop.getDesktop().browse(new URI(GITHUB_LINK));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });

                flowPane.getChildren().addAll(label, githubLink);
                aboutWindow.getDialogPane().contentProperty().setValue(flowPane);

                aboutWindow.show();
            }
        });

    }

}