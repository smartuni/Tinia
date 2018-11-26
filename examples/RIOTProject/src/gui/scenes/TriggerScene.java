package gui.scenes;

import daten.Daten;
import daten.Trigger;
import daten.TriggerRange;
import daten.Windgeschwindigkeit;
import gui.GUI;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TriggerScene implements GUIScene {
    private Scene scene;

    final JFXPanel fxPanel = new JFXPanel();

    private GUI gui;

    public TriggerScene(GUI gui) {
        this.gui = gui;
        VBox layout = new VBox(10);
        this.scene = new Scene(layout, 640, 300);
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.getChildren().addAll(headlineLabel(),addTrigger(),triggerTable(), footerLink());

    }

    private Node triggerTable() {
        TableView table = new TableView();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn triggerName = new TableColumn("Name");
        triggerName.setCellValueFactory(new PropertyValueFactory<Trigger, String>("name"));
        TableColumn triggerType = new TableColumn("Typ");
        triggerType.setCellValueFactory(new PropertyValueFactory<Trigger, String>("triggerType"));
        TableColumn triggerActive = new TableColumn("Status");
        triggerActive.setCellValueFactory(new PropertyValueFactory<Trigger, String>("active"));

        table.getColumns().addAll(triggerName, triggerType, triggerActive);
        table.setItems(gui.getTriggerData());
        System.out.println(gui.getTriggerData());
        return table;
    }

    private Node addTrigger() {
        HBox addTriggerBox = new HBox(10);
        Button newTriggerButton = new Button("+ Trigger anlegen");
        addTriggerBox.getChildren().add(newTriggerButton);
        addTriggerBox.setAlignment(Pos.BASELINE_RIGHT);
        return addTriggerBox;
    }

    private Node headlineLabel() {
        Text text = new Text("Trigger verwalten");
        text.setId("windTextHeadline");
        return text;
    }

    private Node footerLink() {
        Hyperlink linkMonitor = new Hyperlink("Zum Monitor");
        linkMonitor.setBorder(Border.EMPTY);
        linkMonitor.setPadding(new Insets(4, 0, 4, 4));
        linkMonitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindScene().getScene());
            }
        });
        return linkMonitor;
    }


    @Override
    public Scene getScene() {
        return this.scene;
    }

}
