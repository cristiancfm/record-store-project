package com.recordstore.controller;

import com.recordstore.model.Artist;
import com.recordstore.model.Item;
import com.recordstore.RSApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {
    @FXML
    private TextField tfTitle;
    @FXML
    private ComboBox<Artist> cbArtistId;
    @FXML
    private TextField tfFormat;
    @FXML
    private TextField tfGenre;
    @FXML
    private TextField tfYear;
    @FXML
    private TextField tfNoUnits;
    @FXML
    private Button btnAddItem;

    private static EntityManagerFactory emf = RSApplication.getEntityManagerFactory();
    private static EntityManager em = RSApplication.getEntityManager();
    private static EntityTransaction tr = RSApplication.getTransaction();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set artistid combobox elements
        ObservableList<Artist> artistsList = FXCollections.observableArrayList();

        Query query = em.createNamedQuery("Artist.queryAll");
        List resultList = query.getResultList();
        artistsList.addAll(resultList);

        cbArtistId.setItems(artistsList);


        //handle item addition to database
        btnAddItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    areInputsCorrect();
                    
                    Item i = new Item();
                    i.setTitle(tfTitle.getText());
                    i.setArtistid(cbArtistId.getValue().getId());
                    i.setFormat(tfFormat.getText());
                    i.setGenre(tfGenre.getText());
                    i.setYear(Integer.parseInt(tfYear.getText()));
                    i.setNounits(Integer.parseInt(tfNoUnits.getText()));

                    //add element in database
                    tr.begin();
                    em.persist(i);
                    em.flush();
                    tr.commit();

                    //add element in list
                    ItemController.getItemsList().add(i);

                    //close window
                    Node source = (Node) actionEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                    
                } catch (Exception ex) {
                    RSApplication.showErrorAlert(Thread.currentThread(), ex);
                }

            }
        });

    }

    private void areInputsCorrect() throws Exception{

        if(tfTitle.getText().trim().isEmpty() || cbArtistId.getValue() == null || tfFormat.getText().trim().isEmpty() ||
            tfGenre.getText().trim().isEmpty() || tfYear.getText().trim().isEmpty() || tfNoUnits.getText().trim().isEmpty()){
            throw new Exception("There are empty values");
        }

        //check if the year has 4 digits
        if(tfYear.getText().length() != 4){
            throw new NumberFormatException("The year has not 4 digits");
        }
        
        //check if the year is a number
        try {
            int year = Integer.parseInt(tfYear.getText());
        } catch (NumberFormatException e){
            throw e;
        }

        //check if the no. of units is a number
        try {
            int year = Integer.parseInt(tfNoUnits.getText());
        } catch (NumberFormatException e){
            throw e;
        }
    }


}
