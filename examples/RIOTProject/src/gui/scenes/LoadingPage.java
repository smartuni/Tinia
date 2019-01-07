package gui.scenes;

import gui.GUI;
import gui.GUIUtils;
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
        this.scene = new Scene(layout,1540,800);
        layout.setId("pane");
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.setCenter(initText());
        layout.setBottom(initLinks());
    }

    private Node initLinks() {

        Hyperlink overviewPage = new Hyperlink("Übersichtsseite");
        overviewPage.setBorder(Border.EMPTY);
        overviewPage.setPadding(new Insets(0, 5, 15, 25));
        overviewPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getOverviewPage().getScene());
            }
        });

        HBox valueLayout = new HBox(10);
        valueLayout.setSpacing(10);
        valueLayout.getChildren().addAll(overviewPage);
        return valueLayout;
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