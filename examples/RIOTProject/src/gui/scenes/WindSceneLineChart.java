package gui.scenes;

import daten.Daten;
import daten.Windgeschwindigkeit;
import javafx.embed.swing.JFXPanel;

import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;



public class WindSceneLineChart implements GUIScene {
    private Scene scene;
    private Daten daten;
    private XYChart.Series series = new XYChart.Series();
    // wichtig zur Initialisierung
    private final JFXPanel fxPanel = new JFXPanel();
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();


    public WindSceneLineChart(Daten daten) {
        this.daten = daten;
        init();
    }

    public void init() {
        xAxis.setLabel("Zeitpunkt");
        yAxis.setLabel("Geschwindigkeit");

        final LineChart<String,Number> lineChart =
                new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Windgeschwindigkeiten");

        //XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");

        // ForEach Schleife
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            series.getData().add(new XYChart.Data(k.getZeitpunkt().toString(), k.getGeschwindigkeit().intValue()));
        }

        this.scene  = new Scene(lineChart,640,300);
        lineChart.getData().add(series);
    }

    public void updateLineChart() {
        Windgeschwindigkeit g = daten.getWindGeschwindigkeiten().get(daten.getWindGeschwindigkeiten().size()-1);
        series.getData().add(new XYChart.Data(g.getZeitpunkt().toString(), g.getGeschwindigkeit().intValue()));
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
