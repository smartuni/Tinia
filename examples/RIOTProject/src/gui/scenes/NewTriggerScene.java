package gui.scenes;

import daten.Trigger;
import daten.TriggerDataType;
import daten.TriggerRange;
import daten.TriggerType;
import gui.GUI;
import gui.GUIUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class NewTriggerScene implements GUIScene {
    private Scene scene;

    final JFXPanel fxPanel = new JFXPanel();

    private GUI gui;

    public NewTriggerScene(GUI gui) {
        this.gui = gui;
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        this.scene = new Scene(layout, 800, 500);
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.getChildren().addAll(GUIUtils.createFancyHeadline("Neuen Trigger anlegen"), addTriggerForm());

    }

    private Node addTriggerForm() {
        GridPane layout = new GridPane();
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        layout.getColumnConstraints().addAll(col1, col2);

        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(10, 10, 10, 10));

        ComboBox<String> triggerRangeComboBox = new ComboBox<>();
        triggerRangeComboBox.getItems().setAll(TriggerRange.getClearedValues());

        HBox triggerRangeLayout = new HBox(10);
        HBox buttonLayout = new HBox(10);
        TextField triggerRangerNumber = new TextField();
        triggerRangerNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (triggerRangerNumber.getText().length() > 4) {
                    String s = triggerRangerNumber.getText().substring(0, 4);
                    triggerRangerNumber.setText(s);
                }
                triggerRangerNumber.setText(triggerRangerNumber.getText().replaceAll("[^0-9]", ""));
            }
        });
        triggerRangeLayout.getChildren().addAll(triggerRangeComboBox, triggerRangerNumber);


        ComboBox<TriggerType> triggerTypesComboBox = new ComboBox<>();
        triggerTypesComboBox.getItems().setAll(TriggerType.values());

        ComboBox<TriggerDataType> triggerDataTypeComboBox = new ComboBox<>();
        triggerDataTypeComboBox.getItems().setAll(TriggerDataType.values());

        TextField triggerNameTextField = new TextField();
        Button createTriggerButton = new Button("Trigger anlegen");
        Button cancelButton = new Button("Abbrechen");
        buttonLayout.getChildren().addAll(createTriggerButton, cancelButton);

        layout.add(new Text("Name: "), 0, 0);
        layout.add(triggerNameTextField, 1, 0);
        layout.add(new Text("Trigger für Datentyp: "), 0, 1);
        layout.add(triggerDataTypeComboBox, 1, 1);
        layout.add(new Text("Art des Triggers:"), 0, 2);
        layout.add(triggerTypesComboBox, 1, 2);
        layout.add(new Text("Trigger auslösen bei"), 0, 3);
        layout.add(triggerRangeLayout, 1, 3);
        layout.add(buttonLayout, 1, 4);

        EventHandler<ActionEvent> createriggerHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (triggerNameTextField.getText().trim().equals("") || triggerRangerNumber.getText().trim().equals("") || triggerTypesComboBox.getValue() == null || triggerRangeComboBox.getValue() == null || triggerDataTypeComboBox.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validierungsfehler");
                    alert.setHeaderText("Felder nicht vollständig");
                    alert.setContentText("Bitte alle Felder ausfüllen.");
                    alert.showAndWait();
                } else {
                    gui.getTriggerData().add(new Trigger(triggerNameTextField.getText(), triggerTypesComboBox.getValue(), true, TriggerRange.getValue(triggerRangeComboBox.getValue()), triggerDataTypeComboBox.getValue(), Integer.valueOf(triggerRangerNumber.getText())));
                    gui.getStage().setScene(gui.getTriggerScene().getScene());
                }
            }
        };
        createTriggerButton.setOnAction(createriggerHandler);

        EventHandler<ActionEvent> cancelHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gui.getStage().setScene(gui.getTriggerScene().getScene());
            }
        };
        cancelButton.setOnAction(cancelHandler);

        return layout;
    }


    @Override
    public Scene getScene() {
        return this.scene;
    }

}
