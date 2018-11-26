package gui.scenes;

import daten.Daten;
import daten.Trigger;
import daten.TriggerRange;
import daten.Windgeschwindigkeit;
import gui.GUI;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;


public class WindScene implements GUIScene {
    private Scene scene;
    private WindSceneLineChart lineChart;

    final JFXPanel fxPanel = new JFXPanel();

    private Text messungText = new Text();
    private Text minText = new Text();
    private Text maxText = new Text();

    private Windgeschwindigkeit minValue = null;
    private Windgeschwindigkeit maxValue = null;

    private Daten daten;

    private GUI gui;

    public WindScene(Daten daten, GUI gui) {
        this.gui = gui;
        this.daten = daten;
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 20, 10, 20));
        this.scene = new Scene(layout, 800, 500);
        layout.setId("pane");
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.setTop(initHeadline());
        layout.setCenter(initMessung());
        layout.setBottom(initMinMaxValue());
    }

    private Node initMinMaxValue() {

        Hyperlink chartLink = new Hyperlink("Zum Chart");
        chartLink.setBorder(Border.EMPTY);
        chartLink.setPadding(new Insets(0, 0, 4, 0));
        chartLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindLineChart().getScene());
            }
        });


        Hyperlink triggerLink = new Hyperlink("Trigger verwalten");
        triggerLink.setBorder(Border.EMPTY);
        triggerLink.setPadding(new Insets(0, 0, 4, 0));
        triggerLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getTriggerScene().getScene());
            }
        });

        HBox valueLayout = new HBox();
        valueLayout.setSpacing(10);
        valueLayout.getChildren().addAll(minText, maxText, chartLink, triggerLink);
        return valueLayout;
    }

    private Text initHeadline() {
        Text text = new Text();
        text.setId("windTextHeadline");
        text.setText("Tinia - DER Windmesser");
        return text;
    }

    private Text initMessung() {
        messungText.setId("windMessungText");
        return messungText;
    }

    private Text initText() {
        Text text = new Text();
        text.setId("windText");
        text.setText("Willkommen bei Tinia. Bitte warte während wir die Daten empfangen...");
        return text;
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }


    public void updateText() {
        int wert = daten.getWindGeschwindigkeiten().get(daten.getWindGeschwindigkeiten().size() - 1).getGeschwindigkeit();
        if (maxValue == null || maxValue.getGeschwindigkeit() < wert) {
            maxValue = new Windgeschwindigkeit(wert, new Date());
            maxText.setText("Max: " + wert + " km/h");
        }
        if (minValue == null || minValue.getGeschwindigkeit() > wert) {
            minValue = new Windgeschwindigkeit(wert, new Date());
            minText.setText("Min: " + wert + " km/h");
        }
        messungText.setText(wert + " km/h");

        gui.getTriggerData().forEach((t) -> {
            Trigger trigger = (Trigger) t;
            if(trigger.isActive()) {
                boolean triggered = false;
                if (trigger.getTriggerRange() == TriggerRange.TRIGGER_ABOVE) {
                    if (wert > trigger.getValue()) {
                        triggered = true;
                    }
                } else {
                    if (wert < trigger.getValue()) {
                        triggered = true;
                    }
                }
                if (triggered) {
                    final Stage dialog = new Stage();
                    dialog.setTitle(trigger.getName());
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(gui.getStage());
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().addAll(new Text("Wert ist " + trigger.getTriggerRange().getText() + " " + trigger.getValue() + "."), new Text("Trigger ausgelöst: " + new Date().toString()));
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    dialog.setScene(dialogScene);
                    dialog.show();
                }
            }
        });
    }
}
