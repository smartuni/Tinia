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
import javafx.scene.control.*;
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
        this.scene = new Scene(layout,1540,800);
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.getChildren().addAll(GUIUtils.createFancyHeadline("Neuen Trigger anlegen"), infoText(), addTriggerForm());

    }

    private Label infoText() {
        Label label = new Label("Das folgende Formular liefert die Möglichkeit, einen neuen Trigger für Tinia zu definieren. Trigger ermöglichen es, bei einer bestimmten Bedingung ein Event zu werfen. Im Folgenden müssen alle Felder als Pflichtfelder betrachtet werden.");
        label.setWrapText(true);
        return label;
    }

    private Node addTriggerForm() {
        GridPane layout = new GridPane();
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
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
                if (triggerRangerNumber.getText().startsWith("0") && triggerRangerNumber.getText().length() != 1) {
                    String s = triggerRangerNumber.getText().substring(1, triggerRangerNumber.getText().length());
                    triggerRangerNumber.setText(s);
                }
                triggerRangerNumber.setText(triggerRangerNumber.getText().replaceAll("[^0-9]", ""));
            }
        });
        triggerRangeLayout.getChildren().addAll(triggerRangeComboBox, triggerRangerNumber);

        HBox triggerTypeLayout = new HBox(10);

        Text triggerExtraText = new Text();
        TextField triggerExtraTextfield = new TextField();
        triggerExtraText.setVisible(false);
        triggerExtraTextfield.setVisible(false);

        ComboBox<TriggerType> triggerTypesComboBox = new ComboBox<>();
        triggerTypesComboBox.getItems().setAll(TriggerType.values());

        Text triggerTypeDescrptionText = new Text();
        triggerTypeLayout.getChildren().addAll(triggerTypesComboBox, triggerTypeDescrptionText);

        triggerTypesComboBox.valueProperty().addListener(new ChangeListener<TriggerType>() {
            @Override
            public void changed(ObservableValue<? extends TriggerType> observable, TriggerType oldValue, TriggerType newValue) {
                triggerTypeDescrptionText.setText(newValue.getText());
                if (newValue == TriggerType.RUECKGABE) {
                    triggerExtraText.setText("Rückgabe-Wert: ");
                    triggerExtraText.setVisible(true);
                    triggerExtraTextfield.setVisible(true);
                } else if (newValue == TriggerType.EMAIL) {
                    triggerExtraText.setText("Senden an: ");
                    triggerExtraText.setVisible(true);
                    triggerExtraTextfield.setVisible(true);
                } else {
                    triggerExtraText.setVisible(false);
                    triggerExtraTextfield.setVisible(false);
                }
            }
        });

        ComboBox<TriggerDataType> triggerDataTypeComboBox = new ComboBox<>();
        triggerDataTypeComboBox.getItems().setAll(TriggerDataType.values());

        TextField triggerNameTextField = new TextField();
        Button createTriggerButton = new Button("Trigger anlegen");
        Button cancelButton = new Button("Abbrechen");
        buttonLayout.getChildren().addAll(createTriggerButton, cancelButton);

        int rowCounter = 0;

        layout.add(new Text("Name: "), 0, rowCounter);
        layout.add(triggerNameTextField, 1, rowCounter);
        rowCounter++;
        layout.add(new Text("Trigger für Datentyp: "), 0, rowCounter);
        layout.add(triggerDataTypeComboBox, 1, rowCounter);
        rowCounter++;
        layout.add(new Text("Art des Triggers:"), 0, rowCounter);
        layout.add(triggerTypeLayout, 1, rowCounter);
        rowCounter++;
        layout.add(triggerExtraText, 0, rowCounter);
        layout.add(triggerExtraTextfield, 1, rowCounter);
        rowCounter++;
        layout.add(new Text("Trigger auslösen bei"), 0, rowCounter);
        layout.add(triggerRangeLayout, 1, rowCounter);
        rowCounter++;
        layout.add(buttonLayout, 1, rowCounter);

        EventHandler<ActionEvent> createriggerHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (triggerNameTextField.getText().trim().equals("") || triggerRangerNumber.getText().trim().equals("") || triggerTypesComboBox.getValue() == null || triggerRangeComboBox.getValue() == null || triggerDataTypeComboBox.getValue() == null || (triggerTypesComboBox.getValue() == TriggerType.RUECKGABE && triggerExtraTextfield.getText().trim().equals("")) || (triggerTypesComboBox.getValue() == TriggerType.EMAIL && triggerExtraTextfield.getText().trim().equals(""))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validierungsfehler");
                    alert.setHeaderText("Felder nicht vollständig");
                    alert.setContentText("Bitte alle Felder ausfüllen.");
                    alert.showAndWait();
                } else {
                    gui.getTriggerData().add(new Trigger(triggerNameTextField.getText(), triggerTypesComboBox.getValue(), triggerExtraTextfield.getText(), true, TriggerRange.getValue(triggerRangeComboBox.getValue()), triggerDataTypeComboBox.getValue(), Integer.valueOf(triggerRangerNumber.getText())));
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
