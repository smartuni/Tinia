package gui.scenes;

import daten.Daten;
import daten.Windgeschwindigkeit;
import gui.GUI;
import javafx.embed.swing.JFXPanel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Calendar;


public class WindSceneLineChart implements GUIScene {
    private Scene scene;
    private Daten daten;
    private GUI gui;

    private String anzeigeFlag = "aktuell";

    private XYChart.Series series = new XYChart.Series();
    // wichtig zur Initialisierung
    private final JFXPanel fxPanel = new JFXPanel();
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();



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
        series.setName("Geschwindigkeit in km/h");

        // ForEach Schleife
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            series.getData().add(new XYChart.Data(k.getReadableTimestamp(), k.getGeschwindigkeit().intValue()));
        }

        this.scene  = new Scene(layout,800,500);
        lineChart.getData().add(series);

        Hyperlink linkMonitor = new Hyperlink("Zum Monitor");
        linkMonitor.setBorder(Border.EMPTY);
        linkMonitor.setPadding(new Insets(4, 0, 4, 4));
        linkMonitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindScene().getScene());
            }
        });

        layout.getChildren().addAll(buttonLeiste(), lineChart, linkMonitor);

    }

    private Node buttonLeiste() {

        Button aktuell = new Button("Aktuell");
        aktuell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anzeigeFlag = "aktuell";
                updateLineChart();
            }
        });

        Button zehntage = new Button("Letzte 10 Tage");
        zehntage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anzeigeFlag = "zehntage";
                updateLineChart();
            }
        });
        Button letzterMonat = new Button("Letzter Monat");
        letzterMonat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anzeigeFlag = "letztermonat";
                updateLineChart();
            }
        });

        HBox valueLayout = new HBox();
        valueLayout.setSpacing(10);
        valueLayout.getChildren().addAll(aktuell,zehntage,letzterMonat);
        valueLayout.setAlignment(Pos.TOP_CENTER);
        return valueLayout;
    }

    public void updateLineChart() {
        Windgeschwindigkeit g = daten.getWindGeschwindigkeiten().get(daten.getWindGeschwindigkeiten().size()-1);
        series.getData().add(new XYChart.Data(g.getReadableTimestamp(), g.getGeschwindigkeit().intValue()));
    }

    private void setSeriesToAktuelleDaten() {
        series = new XYChart.Series();
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            series.getData().add(new XYChart.Data(k.getReadableTimestamp(), k.getGeschwindigkeit().intValue()));
        }
    }

    private void setSeriesToLetzteZehnTageDaten() {
        series = new XYChart.Series();
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            series.getData().add(new XYChart.Data(k.getReadableTimestamp(), k.getGeschwindigkeit().intValue()));
        }
    }

    private void setSeriesToLetzterMonatDaten() {
        series = new XYChart.Series();
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            Calendar windTime = Calendar.getInstance();
            windTime.setTime(k.getZeitpunkt());
            //if (windTime.get(Calendar.DAY_OF_MONTH) == Calendar.)
            series.getData().add(new XYChart.Data(k.getReadableTimestamp(), k.getGeschwindigkeit().intValue()));
        }
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
