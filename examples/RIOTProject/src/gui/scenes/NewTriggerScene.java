package gui.scenes;

import daten.Trigger;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class NewTriggerScene implements GUIScene {
    private Scene scene;

    final JFXPanel fxPanel = new JFXPanel();

    private GUI gui;

    public NewTriggerScene(GUI gui) {
        this.gui = gui;
        VBox layout = new VBox(10);
        this.scene = new Scene(layout, 800, 500);
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.getChildren().addAll(GUIUtils.createFancyHeadline("Neuen Trigger anlegen"), addTriggerForm());

    }

    private Node addTriggerForm() {
        GridPane layout = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        layout.getColumnConstraints().addAll(col1,col2);

        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(10, 10, 10, 10));

        ComboBox<String> triggerRangeComboBox = new ComboBox<>();
        triggerRangeComboBox.getItems().setAll(TriggerRange.getClearedValues());

        HBox triggerRangeLayout = new HBox(10);
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

        TextField triggerNameTextField = new TextField();
        Button createTriggerButton = new Button("Trigger anlegen");

        layout.add(new Text("Name: "), 0, 0);
        layout.add(triggerNameTextField, 1, 0);
        layout.add(new Text("Art des Triggers"), 0, 1);
        layout.add(triggerTypesComboBox, 1, 1);
        layout.add(new Text("Trigger auslösen bei"), 0, 2);
        layout.add(triggerRangeLayout, 1,2 );
        layout.add(createTriggerButton,1,3);

        EventHandler<ActionEvent> createriggerHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gui.getTriggerData().add(new Trigger(triggerNameTextField.getText(), triggerTypesComboBox.getValue(), true, TriggerRange.getValue(triggerRangeComboBox.getValue()), Integer.valueOf(triggerRangerNumber.getText())));
                gui.getStage().setScene(gui.getTriggerScene().getScene());
            }
        };
        createTriggerButton.setOnAction(createriggerHandler);

        return layout;
    }


    @Override
    public Scene getScene() {
        return this.scene;
    }

}
