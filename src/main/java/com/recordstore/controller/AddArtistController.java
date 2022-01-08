package com.recordstore.controller;

import com.recordstore.model.Artist;
import com.recordstore.RSApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.net.URL;
import java.util.ResourceBundle;

public class AddArtistController implements Initializable {
    @FXML
    private TextField tfName;
    @FXML
    private Button btnAddArtist;

    private static EntityManagerFactory emf = RSApplication.getEntityManagerFactory();
    private static EntityManager em = RSApplication.getEntityManager();
    private static EntityTransaction tr = RSApplication.getTransaction();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //handle artist addition to database
        btnAddArtist.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    areInputsCorrect();

                    Artist a = new Artist();
                    a.setName(tfName.getText());

                    //add element in database
                    tr.begin();
                    em.persist(a);
                    em.flush();
                    tr.commit();

                    //add element in list
                    ArtistController.getArtistsList().add(a);

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

        if(tfName.getText().trim().isEmpty()){
            throw new Exception("There are empty values");
        }

    }


}
