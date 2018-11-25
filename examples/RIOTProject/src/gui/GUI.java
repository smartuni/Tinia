package gui;

import daten.Daten;
import gui.scenes.InitScene;
import gui.scenes.WindScene;
import gui.scenes.WindSceneLineChart;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import mqtt.MqttConnection;

public class GUI extends Application {

    private Stage stage;

    private Daten daten = new Daten();

    private InitScene initScene = new InitScene();
    private WindScene windScene = new WindScene(daten);
    private WindSceneLineChart windLineChart = new WindSceneLineChart(daten);



    @Override
    public void init() {
        try {
            new MqttConnection("eu", "tini-riot-ws-1819", "ttn-account-v2.1eClE8ktJ5Js0gpZxIX1AifwEEnhDpPJ4ag24jyKdrE", this, daten);
        } catch (Exception e) {
            System.err.println("Fehler in gui.GUI!");
            e.printStackTrace();

        }
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

    public void updateText(String message) {
        Platform.runLater(() -> {
            if (stage.getScene() != windScene.getScene()) {
                initScene.stopTimer();
                stage.setScene(windScene.getScene());
            }
            windScene.updateText(message);
        });
    }

    public void updateGui() {
        Platform.runLater(() -> {
            if (stage.getScene() != windScene.getScene()) {
                stage.setScene(windScene.getScene());
            }
            windScene.updateText(daten.getWindGeschwindigkeiten().get(daten.getWindGeschwindigkeiten().size()-1).getGeschwindigkeit().toString());
            windLineChart.updateLineChart();
        });
    }


    public void run() {
        Application.launch();
    }

}