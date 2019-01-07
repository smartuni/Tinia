package gui.scenes;

import daten.Daten;
import daten.Trigger;
import daten.TriggerRange;
import daten.Windrichtung;
import gui.GUI;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.Optional;


public class WindrichtungTableScene implements GUIScene {
    private VBox layout;
    private Scene scene;
    private Daten daten;
    private TableView<Windrichtung> table;
    private ArrayList<Windrichtung> data;
    private ObservableList<Windrichtung> windrichtungen;


    final JFXPanel fxPanel = new JFXPanel();

    private GUI gui;

    public WindrichtungTableScene(Daten daten,GUI gui) {
        this.gui = gui;
        this.daten = daten;
        layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        this.scene = new Scene(layout,1540,800);
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());

        Hyperlink linkMonitor = new Hyperlink("Zurück zur Übersicht");
        linkMonitor.setBorder(Border.EMPTY);
        linkMonitor.setPadding(new Insets(4, 0, 4, 4));
        linkMonitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getOverviewPage().getScene());
            }
        });

        Hyperlink linkZurueck = new Hyperlink("Zurück");
        linkZurueck.setBorder(Border.EMPTY);
        linkZurueck.setPadding(new Insets(4, 0, 4, 4));
        linkZurueck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindRichtungScene().getScene());
            }
        });

        layout.getChildren().addAll(windrichtungTable(),linkZurueck,linkMonitor);
    }

    private Node windrichtungTable() {

        table = new TableView<>();
        data = daten.getWindrichtungen();
        windrichtungen = FXCollections.observableArrayList(data);
        table.setItems(windrichtungen);

        TableColumn datumCol = new TableColumn("Datum");
        datumCol.setCellValueFactory(new PropertyValueFactory<Windrichtung, String>("readableZeitpunkt"));
        TableColumn windrichtungCol = new TableColumn("Windrichtung");
        windrichtungCol.setCellValueFactory(new PropertyValueFactory<Windrichtung, String>("readableWindrichtung"));

        table.getColumns().setAll(datumCol, windrichtungCol);
        table.setPrefWidth(450);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        return table;
    }

    public void updateTable() {
        Windrichtung g = daten.getWindrichtungen().get(daten.getWindrichtungen().size()-1);
        windrichtungen.add(g);
    }



    @Override
    public Scene getScene() {
        return this.scene;
    }

}
