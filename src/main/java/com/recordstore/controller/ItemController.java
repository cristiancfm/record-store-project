package com.recordstore.controller;

import com.recordstore.exceptions.ArtistNotFoundException;
import com.recordstore.model.Artist;
import com.recordstore.model.Item;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import javax.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ItemController implements Initializable {
    @FXML
    public TableView<Item> tvItems;
    @FXML
    private TableColumn<Item, Integer> colItemId;
    @FXML
    private TableColumn<Item, String> colTitle;
    @FXML
    private TableColumn<Item, Integer> colItemArtistId;
    @FXML
    private TableColumn<Item, String> colFormat;
    @FXML
    private TableColumn<Item, String> colGenre;
    @FXML
    private TableColumn<Item, Integer> colYear;
    @FXML
    private TableColumn<Item, Integer> colNoUnits;
    @FXML
    private Button btnItemAdd;
    @FXML
    private Button btnItemDelete;
    @FXML
    private TextArea textItemInfo;


    private static EntityManagerFactory emf = RSApplication.getEntityManagerFactory();
    private static EntityManager em = RSApplication.getEntityManager();
    private static EntityTransaction tr = RSApplication.getTransaction();

    private static ObservableList<Item> itemsList = FXCollections.observableArrayList();

    public static ObservableList<Item> getItemsList(){
        return itemsList;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showItems();

    }

    public ObservableList<Item> initializeItemsList(){
        Query query = em.createNamedQuery("Item.queryAll");
        List resultList = query.getResultList();
        itemsList.addAll(resultList);
        return itemsList;
    }


    public void showItems(){
        ObservableList<Item> list = initializeItemsList();

        //get items properties
        colItemId.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Item, String>("title"));
        colItemArtistId.setCellValueFactory(new PropertyValueFactory<Item, Integer>("artistid"));
        colFormat.setCellValueFactory(new PropertyValueFactory<Item, String>("format"));
        colGenre.setCellValueFactory(new PropertyValueFactory<Item, String>("genre"));
        colYear.setCellValueFactory(new PropertyValueFactory<Item, Integer>("year"));
        colNoUnits.setCellValueFactory(new PropertyValueFactory<Item, Integer>("nounits"));


        //change fields to be editable
        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        colItemArtistId.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colFormat.setCellFactory(TextFieldTableCell.forTableColumn());
        colGenre.setCellFactory(TextFieldTableCell.forTableColumn());
        colYear.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colNoUnits.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        //handle edited fields
        colTitle.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Item, String> event) {
                Item item = event.getRowValue();
                item.setTitle(event.getNewValue());
                tr.begin();
                em.merge(item);
                tr.commit();
            }
        });
        colItemArtistId.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Item, Integer> event) {

                //look for the artist id in the database
                TypedQuery<Artist> query = em.createNamedQuery("Artist.queryById", Artist.class);
                query.setParameter("id", event.getNewValue());
                List resultList = query.getResultList();

                if(resultList.isEmpty()){
                    RSApplication.showErrorAlert(Thread.currentThread(), new ArtistNotFoundException());
                    tvItems.setItems(ItemController.getItemsList());
                } else{
                    Item item = event.getRowValue();
                    item.setArtistid(event.getNewValue());

                    tr.begin();
                    em.merge(item);
                    tr.commit();
                }
            }
        });
        colFormat.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Item, String> event) {
                Item item = event.getRowValue();
                item.setFormat(event.getNewValue());
                tr.begin();
                em.merge(item);
                tr.commit();
            }
        });
        colGenre.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Item, String> event) {
                Item item = event.getRowValue();
                item.setGenre(event.getNewValue());
                tr.begin();
                em.merge(item);
                tr.commit();
            }
        });
        colYear.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Item, Integer> event) {
                Item item = event.getRowValue();
                item.setYear(event.getNewValue());
                tr.begin();
                em.merge(item);
                tr.commit();
            }
        });
        colNoUnits.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Item, Integer> event) {
                Item item = event.getRowValue();
                item.setNounits(event.getNewValue());
                tr.begin();
                em.merge(item);
                tr.commit();
            }
        });

        //delete selected rows when pressing DELETE key
        tvItems.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.DELETE){
                    ObservableList<Item> selectedItems = tvItems.getSelectionModel().getSelectedItems();

                    if(selectedItems.isEmpty()){
                        RSApplication.showInfoAlert(new NoRowSelectedException());
                    }

                    //delete elements in database
                    for (Item item : selectedItems) {
                        tr.begin();
                        em.remove(item);
                        tr.commit();
                    }

                    //delete elements in table
                    selectedItems.forEach(row -> tvItems.getItems().remove(row));
                }
            }
        });

        btnItemAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(RSApplication.class.getResource("add-item-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 250, 300);
                    Stage stage = new Stage();
                    stage.setTitle("Add Item");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    RSApplication.showErrorAlert(Thread.currentThread(), e);
                }

            }
        });

        //delete selected rows when Delete button is used
        btnItemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<Item> selectedItems = tvItems.getSelectionModel().getSelectedItems();

                if(selectedItems.isEmpty()){
                    RSApplication.showInfoAlert(new NoRowSelectedException());
                }

                //delete elements in database
                for (Item item : selectedItems) {
                    tr.begin();
                    em.remove(item);
                    tr.commit();
                }

                //delete elements in table
                selectedItems.forEach(row -> tvItems.getItems().remove(row));
            }
        });


        //change information text when selecting a new row
        tvItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {
            @Override
            public void changed(ObservableValue<? extends Item> observableValue, Item oldItem, Item newItem) {
                if(newItem != null){
                    textItemInfo.setText(newItem.getInfo());
                }
            }
        });


        tvItems.setItems(list);
        tvItems.setEditable(true);
        tvItems.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}
