package com.recordstore.controller;

import com.recordstore.model.Artist;
import com.recordstore.RSApplication;
import com.recordstore.exceptions.NoRowSelectedException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArtistController implements Initializable {
    @FXML
    private TableView<Artist> tvArtists;
    @FXML
    private TableColumn<Artist, Integer> colArtistId;
    @FXML
    private TableColumn<Artist, String> colName;
    @FXML
    private Button btnArtistAdd;
    @FXML
    private Button btnArtistDelete;
    @FXML
    private TextArea textArtistInfo;


    private static EntityManagerFactory emf = RSApplication.getEntityManagerFactory();
    private static EntityManager em = RSApplication.getEntityManager();
    private static EntityTransaction tr = RSApplication.getTransaction();

    private static ObservableList<Artist> artistsList = FXCollections.observableArrayList();

    public static ObservableList<Artist> getArtistsList(){
        return artistsList;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showArtists();

    }


    public ObservableList<Artist> initializeArtistsList(){
        Query query = em.createNamedQuery("Artist.queryAll");
        List resultList = query.getResultList();
        artistsList.addAll(resultList);
        return artistsList;
    }


    public void showArtists(){
        ObservableList<Artist> list = initializeArtistsList();

        //get artists properties
        colArtistId.setCellValueFactory(new PropertyValueFactory<Artist, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Artist, String>("name"));


        //change fields to be editable
        colName.setCellFactory(TextFieldTableCell.forTableColumn());

        //handle edited fields
        colName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Artist, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Artist, String> event) {
                Artist artist = event.getRowValue();
                artist.setName(event.getNewValue());
                tr.begin();
                em.merge(artist);
                tr.commit();
            }
        });

        //delete selected rows when pressing DELETE key
        tvArtists.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.DELETE){
                    ObservableList<Artist> selectedArtists = tvArtists.getSelectionModel().getSelectedItems();

                    if(selectedArtists.isEmpty()){
                        RSApplication.showInfoAlert(new NoRowSelectedException());
                    }

                    //delete elements in database
                    for (Artist artist : selectedArtists) {
                        tr.begin();
                        em.remove(artist);
                        tr.commit();
                    }

                    //delete elements in table
                    selectedArtists.forEach(row -> tvArtists.getItems().remove(row));
                }
            }
        });

        btnArtistAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(RSApplication.class.getResource("add-artist-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 250, 100);
                    Stage stage = new Stage();
                    stage.setTitle("Add Artist");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    RSApplication.showErrorAlert(Thread.currentThread(), e);
                }

            }
        });

        //delete selected rows when Delete button is used
        btnArtistDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<Artist> selectedArtists = tvArtists.getSelectionModel().getSelectedItems();

                if(selectedArtists.isEmpty()){
                    RSApplication.showInfoAlert(new NoRowSelectedException());
                }

                //delete elements in database
                for (Artist artist : selectedArtists) {
                    tr.begin();
                    em.remove(artist);
                    tr.commit();
                }

                //delete elements in table
                selectedArtists.forEach(row -> tvArtists.getItems().remove(row));
            }
        });


        //change information text when selecting a new row
        tvArtists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Artist>() {
            @Override
            public void changed(ObservableValue<? extends Artist> observableValue, Artist oldArtist, Artist newArtist) {
                if(newArtist != null){
                    textArtistInfo.setText(newArtist.getInfo());
                }
            }
        });


        tvArtists.setItems(list);
        tvArtists.setEditable(true);
        tvArtists.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
