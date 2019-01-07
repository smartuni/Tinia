package gui;

import daten.*;
import gui.scenes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import mqtt.MqttConnection;

public class GUI extends Application {

    private Stage stage;

    private Daten daten = new Daten();

    private LoadingPage initScene;
    private WindScene windScene = new WindScene(daten, this);
    private WindRichtungScene windRichtungScene = new WindRichtungScene(daten,this);
    private WindrichtungTableScene windrichtungTableScene = new WindrichtungTableScene(daten,this);
    private TriggerScene triggerScene;

    private OverviewPage overviewPage;
    private WindSceneLineChart windLineChart = new WindSceneLineChart(daten, this);

    private final ObservableList<Trigger> data =
            FXCollections.observableArrayList(
                    new Trigger("Warnung", TriggerType.MELDUNG, "", false, TriggerRange.TRIGGER_ABOVE, TriggerDataType.WINDGESCHWINDIGKEIT, 50)
            );

    public WindScene getWindScene() {
        return this.windScene;
    }

    public ObservableList getTriggerData() {
        return data;
    }

    public WindSceneLineChart getWindLineChart() {
        return this.windLineChart;
    }

    public WindRichtungScene getWindRichtungScene() {
        return windRichtungScene;
    }

    public OverviewPage getOverviewPage() {
        return overviewPage;
    }

    public void setOverviewPage(OverviewPage overviewPage) {
        this.overviewPage = overviewPage;
    }

    public TriggerScene getTriggerScene() {
        return this.triggerScene;
    }

    public WindrichtungTableScene getWindrichtungTableScene() { return this.windrichtungTableScene;}

    @Override
    public void init() {
        initScene = new LoadingPage(this);
        triggerScene = new TriggerScene(this);
        overviewPage = new OverviewPage(daten, this);
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