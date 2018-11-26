package gui.scenes;

import daten.Daten;
import daten.Trigger;
import daten.TriggerRange;
import daten.Windgeschwindigkeit;
import gui.GUI;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
        this.scene = new Scene(layout, 800, 500);
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.getChildren().addAll(headlineLabel(),addTrigger(),triggerTable(), footerLink());

    }

    private Node triggerTable() {
        TableView table = new TableView();
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn triggerName = new TableColumn("Name");
        triggerName.setCellValueFactory(new PropertyValueFactory<Trigger, String>("name"));
        triggerName.setCellFactory(TextFieldTableCell.<Trigger> forTableColumn());
        triggerName.setOnEditCommit(
                new EventHandler<CellEditEvent<Trigger, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Trigger, String> t) {
                        ((Trigger) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );
        TableColumn triggerType = new TableColumn("Typ");
        triggerType.setCellValueFactory(new PropertyValueFactory<Trigger, String>("triggerType"));
        TableColumn triggerActive = new TableColumn("Aktiviert");
        triggerActive.setCellValueFactory(new PropertyValueFactory<Trigger, Boolean>("active"));

        triggerActive.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Trigger, Boolean>, ObservableValue<Boolean>>) param -> {
            Trigger trigger = param.getValue();

            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(trigger.isActive());
            booleanProp.addListener((observable, oldValue, newValue) -> trigger.setActive(newValue));
            return booleanProp;
        });

        //
        triggerActive.setCellFactory((Callback<TableColumn<Trigger, Boolean>, TableCell<Trigger, Boolean>>) p -> {
            CheckBoxTableCell<Trigger, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        TableColumn triggerCondition = new TableColumn("Bedingung");
        TableColumn triggerRange = new TableColumn("Über/Unter");
        triggerRange.setCellValueFactory(new PropertyValueFactory<Trigger, String>("triggerRangeReadable"));
        TableColumn triggerValue = new TableColumn("Wert");
        triggerValue.setCellValueFactory(new PropertyValueFactory<Trigger, String>("value"));
        triggerCondition.getColumns().addAll(triggerRange, triggerValue);

        table.getColumns().addAll(triggerName, triggerType, triggerActive, triggerCondition);
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
