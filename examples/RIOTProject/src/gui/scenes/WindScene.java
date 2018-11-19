package gui.scenes;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class WindScene implements GUIScene {
    private Scene scene;
    private Text messungText = new Text();
    private Text minText = new Text();
    private Text maxText = new Text();

    private int minValue = Integer.MAX_VALUE;
    private int maxValue = 0;

    public WindScene() {

        BorderPane layout = new BorderPane();
        this.scene = new Scene(layout, 640, 300);
        layout.setTop(initHeadline());
        layout.setCenter(initMessung());
        layout.setBottom(initMinMaxValue());
    }

    private Node initMinMaxValue() {
        HBox valueLayout = new HBox();
        valueLayout.getChildren().addAll(minText, maxText);
        return valueLayout;
    }

    private Text initHeadline() {
        Text text = new Text();
        text.setText("Tinia - DER Windmesser");
        return text;
    }

    private Text initMessung() {
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
        if(wert > maxValue) {
            maxValue = wert;
            maxText.setText("Max: " + wert + " km/h");
        }
        if(wert < minValue) {
            minValue = wert;
            minText.setText("Min: " + wert + " km/h");
        }
        messungText.setText(message + " km/h");
    }
}
