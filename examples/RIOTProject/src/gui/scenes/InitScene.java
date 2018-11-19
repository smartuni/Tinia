package gui.scenes;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;


public class InitScene implements GUIScene {
    private Scene scene;

    public InitScene() {
        BorderPane layout = new BorderPane();
        this.scene = new Scene(layout, 640, 300);
        layout.setCenter(initText());
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
}
