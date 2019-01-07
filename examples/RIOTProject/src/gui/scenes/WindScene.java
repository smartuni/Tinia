package gui.scenes;

import daten.*;
import gui.GUI;
import gui.GUIUtils;
import gui.dialogs.TriggerDialog;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

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
        triggerLink.setId("linkLeiste");
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
        valueLayout.getChildren().addAll(minText, maxText, chartLink, triggerLink, GUIUtils.overviewLink(gui));
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
                } else if(trigger.getTriggerRange() == TriggerRange.TRIGGER_UNDER) {
                    if (wert < trigger.getValue()) {
                        triggered = true;
                    }
                }
                if (triggered) {
                    if(trigger.getTriggerType() == TriggerType.MELDUNG) {
                        new TriggerDialog(trigger, gui, wert);
                    }
                }
            }
        });
    }
}
