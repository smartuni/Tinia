package gui.scenes;

import gui.daten.Windgeschwindigkeit;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Date;


public class WindScene implements GUIScene {
    private Scene scene;
    private Text messungText = new Text();
    private Text minText = new Text();
    private Text maxText = new Text();

    private Windgeschwindigkeit minValue = null;
    private Windgeschwindigkeit maxValue = null;

    public WindScene() {

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
        HBox valueLayout = new HBox();
        valueLayout.setSpacing(10);
        valueLayout.getChildren().addAll(minText, maxText);
        return valueLayout;
    }

    private Text initHeadline() {
        Text text = new Text();
        text.setText("Tinia - DER Windmesser");
        text.setStyle("-fx-font-weight: bold");
        return text;
    }

    private Text initMessung() {
        messungText.setFont(Font.font("Verdana", FontWeight.BLACK.BOLD, 70));
        return messungText;
    }

    private Text initText() {
        Text text = new Text();
        text.setText("Willkommen bei Tinia. Bitte warte während wir die Daten empfangen...");
        return text;
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }

    public void updateText(String message) {
        int wert = Integer.valueOf(message);
        if(maxValue == null || maxValue.getGeschwindigkeit() < wert) {
            maxValue = new Windgeschwindigkeit(wert, new Date());
            maxText.setText("Max: " + wert + " km/h");
        }
        if(minValue == null || minValue.getGeschwindigkeit() > wert) {
            minValue = new Windgeschwindigkeit(wert, new Date());
            minText.setText("Min: " + wert + " km/h");
        }
        messungText.setText(message + " km/h");
    }
}
