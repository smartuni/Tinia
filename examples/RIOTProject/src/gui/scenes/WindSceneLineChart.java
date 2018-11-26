package gui.scenes;

import daten.Daten;
import daten.Windgeschwindigkeit;
import gui.GUI;
import javafx.embed.swing.JFXPanel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;


public class WindSceneLineChart implements GUIScene {
    private Scene scene;
    private Daten daten;
    private XYChart.Series series = new XYChart.Series();
    // wichtig zur Initialisierung
    private final JFXPanel fxPanel = new JFXPanel();
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private GUI gui;


    public WindSceneLineChart(Daten daten, GUI gui) {
        this.gui = gui;
        this.daten = daten;
        init();
    }

    public void init() {
        VBox layout = new VBox();
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

        this.scene  = new Scene(layout,640,300);
        lineChart.getData().add(series);
        Hyperlink linkMonitor = new Hyperlink("Zum Monitor");
        linkMonitor.setBorder(Border.EMPTY);
        linkMonitor.setPadding(new Insets(4, 0, 4, 0));
        linkMonitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindScene().getScene());
            }
        });
        layout.getChildren().addAll(lineChart, linkMonitor);

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
