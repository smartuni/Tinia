package gui;

import daten.Daten;
import gui.scenes.InitScene;
import gui.scenes.WindScene;
import gui.scenes.WindSceneLineChart;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import mqtt.MqttConnection;

public class GUI extends Application {

    private Stage stage;

    private Daten daten = new Daten();

    private InitScene initScene = new InitScene();
    private WindScene windScene = new WindScene(daten, this);
    private WindSceneLineChart windLineChart = new WindSceneLineChart(daten, this);

    public WindScene getWindScene() {
        return this.windScene;
    }

    public WindSceneLineChart getWindLineChart() {
        return this.windLineChart;
    }

    @Override
    public void init() {
        try {
            new MqttConnection("eu", "tini-riot-ws-1819", "ttn-account-v2.1eClE8ktJ5Js0gpZxIX1AifwEEnhDpPJ4ag24jyKdrE", this, daten);
        } catch (Exception e) {
            System.err.println("Fehler in gui.GUI!");
            e.printStackTrace();

        }
    }

    public Stage getStage() {
        return this.stage;
    }


    @Override
    public void start(Stage stage) {
        stage.setTitle("Tinia");
        stage.setScene(initScene.getScene());
        //stage.setScene(windLineChart.getScene());
        stage.setOnCloseRequest(event -> System.exit(0));
        this.stage = stage;

        //Displaying the contents of the stage
        stage.show();
    }

    public void updateGui() {

        Platform.runLater(() -> {
            if (stage.getScene() == initScene.getScene()) {
                stage.setScene(windScene.getScene());
            }
            windScene.updateText();
            windLineChart.updateLineChart();
        });
    }


    public void run() {
        Application.launch();
    }

}