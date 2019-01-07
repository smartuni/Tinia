package gui.scenes;

import gui.GUI;
import gui.GUIUtils;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;


public class LoadingPage implements GUIScene {
    private Scene scene;
    private Timer timer;
    private GUI gui;

    final JFXPanel fxPanel = new JFXPanel();

    public LoadingPage(GUI gui) {
        this.gui = gui;
        BorderPane layout = new BorderPane();
        this.scene = new Scene(layout, 800, 500);
        layout.setId("pane");
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.setCenter(initText());
        layout.setBottom(GUIUtils.overviewLink(gui));
    }

    private Node initText() {
        Text text = new Text();
        text.setId("windInitText");
        text.setText("Willkommen bei Tinia!\nWir warten noch kurz bis wir Daten empfangen");
        timer = new Timer(true);
        timer.schedule(new ShowLoading(text), 0, 1111);
        return text;
    }

    public void stopTimer() {
        timer.cancel();
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}

class ShowLoading extends TimerTask {
    private Text text;
    public int add = 0;

    public ShowLoading(Text text) {
        this.text = text;
    }

    public void run() {
        if (add < 3) {
            text.setText(text.getText() + ".");
            add++;
        } else {
            add = 0;
            text.setText(text.getText().substring(0, text.getText().length() - 3));
        }
    }
}