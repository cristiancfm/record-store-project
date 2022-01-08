package com.recordstore;

import com.recordstore.model.Artist;
import com.recordstore.model.Item;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RSApplication extends Application {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("record-store-jpa");
    private static final EntityManager em = emf.createEntityManager();
    private static final EntityTransaction tr =  em.getTransaction();

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
    public static EntityManager getEntityManager() {
        return em;
    }
    public static EntityTransaction getTransaction() {
        return tr;
    }


    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(RSApplication::showErrorAlert);

        //read command arguments
        Parameters params = getParameters();
        List<String> paramsList = params.getRaw();
        if(paramsList.contains("--add-examples")){
            addExamples();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(RSApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        stage.getIcons().add(new Image(new File("src/main/resources/cd_case.png").toURI().toString()));
        stage.setTitle("Record Store");
        stage.setScene(scene);
        stage.show();

        //free database resources at the end
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                em.close();
                emf.close();
            }
        });
    }

    public static void showErrorAlert(Thread t, Throwable e) {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());
            alert.showAndWait();
        } else {
            System.err.println("An unexpected error occurred in " + t);
        }
    }

    public static void showInfoAlert(Throwable e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information");
        alert.setContentText(e.toString());
        alert.showAndWait();
    }

    private void addExamples(){
        Artist a1 = new Artist();
        a1.setName("Foo Fighters");
        Artist a2 = new Artist();
        a2.setName("Shakira");

        Item i1 = new Item();
        i1.setTitle("One By One");
        i1.setArtistid(1);
        i1.setFormat("CD");
        i1.setGenre("Rock");
        i1.setYear(2002);
        i1.setNounits(2);

        tr.begin();
        em.persist(a1);
        em.persist(a2);
        tr.commit();

        tr.begin();
        em.persist(i1);
        tr.commit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}