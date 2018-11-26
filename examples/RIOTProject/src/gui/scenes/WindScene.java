package gui.scenes;

import daten.Daten;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        this.scene = new Scene(layout, 640, 300);
        layout.setId("pane");
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.setTop(initHeadline());
        layout.setCenter(initMessung());
        layout.setBottom(initMinMaxValue());
    }

    private Node initMinMaxValue() {

        Hyperlink chartLink = new Hyperlink("Zum Chart");
        chartLink.setBorder(Border.EMPTY);
        chartLink.setPadding(new Insets(4, 0, 4, 0));
        chartLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindLineChart().getScene());
            }
        });
        HBox valueLayout = new HBox();
        valueLayout.setSpacing(10);
        valueLayout.getChildren().addAll(minText, maxText, chartLink);
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
        int wert = daten.getWindGeschwindigkeiten().get(daten.getWindGeschwindigkeiten().size()-1).getGeschwindigkeit();
        if(maxValue == null || maxValue.getGeschwindigkeit() < wert) {
            maxValue = new Windgeschwindigkeit(wert, new Date());
            maxText.setText("Max: " + wert + " km/h");
        }
        if(minValue == null || minValue.getGeschwindigkeit() > wert) {
            minValue = new Windgeschwindigkeit(wert, new Date());
            minText.setText("Min: " + wert + " km/h");
        }
        messungText.setText(wert + " km/h");
    }
}
