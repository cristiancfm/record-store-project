package com.recordstore;

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

        FXMLLoader fxmlLoader = new FXMLLoader(RSApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
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

    public static void main(String[] args) {
        launch();
    }
}