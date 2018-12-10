package gui.scenes;

import daten.Trigger;
import daten.TriggerRange;
import gui.GUI;
import gui.GUIUtils;
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

import java.util.Optional;


public class TriggerScene implements GUIScene {
    private Scene scene;

    final JFXPanel fxPanel = new JFXPanel();

    private GUI gui;

    public TriggerScene(GUI gui) {
        this.gui = gui;
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        this.scene = new Scene(layout, 800, 500);
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.getChildren().addAll(GUIUtils.createFancyHeadline("Trigger verwalten"), addTrigger(), triggerTable(), footerLink());

    }

    private Node triggerTable() {
        TableView table = new TableView();
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn triggerName = new TableColumn("Name");
        triggerName.setCellValueFactory(new PropertyValueFactory<Trigger, String>("name"));
        triggerName.setCellFactory(TextFieldTableCell.<Trigger>forTableColumn());
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
        TableColumn triggerDataType = new TableColumn("Daten-Typ");
        triggerDataType.setCellValueFactory(new PropertyValueFactory<Trigger, String>("triggerDataType"));
        TableColumn triggerType = new TableColumn("Typ");
        TableColumn triggerTypeType = new TableColumn("Typ");
        triggerTypeType.setCellValueFactory(new PropertyValueFactory<Trigger, String>("triggerType"));
        TableColumn triggerTypeExtra = new TableColumn("Zusatz");
        triggerTypeExtra.setCellValueFactory(new PropertyValueFactory<Trigger, String>("extraTypeValue"));
        triggerType.getColumns().addAll(triggerTypeType, triggerTypeExtra);
        TableColumn triggerActive = new TableColumn("Aktiviert");
        triggerActive.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Trigger, Boolean>, ObservableValue<Boolean>>) param -> {
            Trigger trigger = param.getValue();

            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(trigger.isActive());
            booleanProp.addListener((observable, oldValue, newValue) -> trigger.setActive(newValue));
            return booleanProp;
        });

        triggerActive.setCellFactory((Callback<TableColumn<Trigger, Boolean>, TableCell<Trigger, Boolean>>) p -> {
            CheckBoxTableCell<Trigger, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        TableColumn triggerCondition = new TableColumn("Bedingung");
        TableColumn triggerRange = new TableColumn("Über/Unter");

        triggerRange.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Trigger, String>, ObservableValue<String>>) param -> {
            Trigger trigger = param.getValue();
            String gender = trigger.getTriggerRange().getText();
            return new SimpleObjectProperty<>(gender);
        });

        ObservableList<String> genderList = FXCollections.observableArrayList(//
                TriggerRange.getClearedValues());

        triggerRange.setCellFactory(ComboBoxTableCell.forTableColumn(genderList));


        triggerRange.setOnEditCommit(new EventHandler<CellEditEvent<Trigger, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Trigger, String> event) {
                TablePosition<Trigger, String> pos = event.getTablePosition();

                TriggerRange newGender = TriggerRange.getValue(event.getNewValue());

                int row = pos.getRow();
                Trigger person = event.getTableView().getItems().get(row);

                person.setTriggerRange(newGender);
            }
        });


        TableColumn triggerValue = new TableColumn("Wert");
        triggerValue.setCellValueFactory(new PropertyValueFactory<Trigger, Integer>("value"));
        triggerValue.setCellFactory(TextFieldTableCell.<Trigger, Integer>forTableColumn(new IntegerStringConverter()));
        triggerValue.setOnEditCommit(
                new EventHandler<CellEditEvent<Trigger, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Trigger, Integer> t) {
                        ((Trigger) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setValue(t.getNewValue());
                    }
                }
        );
        triggerCondition.getColumns().addAll(triggerRange, triggerValue);

        TableColumn actionCol = new TableColumn("Action");
        //actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        Callback<TableColumn<Trigger, String>, TableCell<Trigger, String>> cellFactory
                = //
                new Callback<TableColumn<Trigger, String>, TableCell<Trigger, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Trigger, String> param) {
                        final TableCell<Trigger, String> cell = new TableCell<Trigger, String>() {

                            final Button btn = new Button("Entfernen");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Bestätigen");
                                        alert.setHeaderText("Wirklich löschen?");
                                        alert.setContentText("Soll der Trigger wirklich gelöscht werden?");

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == ButtonType.OK) {
                                            Trigger triggerObject = getTableView().getItems().get(getIndex());
                                            gui.getTriggerData().remove(triggerObject);
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        actionCol.setCellFactory(cellFactory);


        table.getColumns().addAll(triggerActive, triggerName, triggerDataType, triggerType, triggerCondition, actionCol);
        table.setItems(gui.getTriggerData());
        System.out.println(gui.getTriggerData());
        return table;
    }

    private Node addTrigger() {
        HBox addTriggerBox = new HBox(10);
        Button newTriggerButton = new Button("+ Trigger anlegen");
        EventHandler<ActionEvent> newTriggerHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gui.getStage().setScene(new NewTriggerScene(gui).getScene());
            }
        };
        newTriggerButton.setOnAction(newTriggerHandler);
        addTriggerBox.getChildren().add(newTriggerButton);
        addTriggerBox.setAlignment(Pos.BASELINE_RIGHT);
        return addTriggerBox;
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
