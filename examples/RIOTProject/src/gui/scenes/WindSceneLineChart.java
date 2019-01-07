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
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class WindSceneLineChart implements GUIScene {
    private Scene scene;
    private Daten daten;
    private GUI gui;

    private String anzeigeFlag = "aktuell";

    private XYChart.Series series = new XYChart.Series();
    private XYChart.Series seriesTenDays = new XYChart.Series();
    private XYChart.Series seriesMonth = new XYChart.Series();
    // wichtig zur Initialisierung
    private final JFXPanel fxPanel = new JFXPanel();
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final LineChart<String,Number> lineChart =
            new LineChart<>(xAxis,yAxis);



    public WindSceneLineChart(Daten daten, GUI gui) {
        this.gui = gui;
        this.daten = daten;
        init();
    }

    public void init() {
        VBox layout = new VBox();
        xAxis.setLabel("Zeitpunkt");
        yAxis.setLabel("Geschwindigkeit");

        lineChart.setTitle("Windgeschwindigkeiten");
        lineChart.setAnimated(false);

        //XYChart.Series series = new XYChart.Series();
        series.setName("Aktuell: Geschwindigkeit in km/h");
        seriesTenDays.setName("Letzte 10 Tage: Geschwindigkeit in km/h");
        seriesMonth.setName("Letzter Monat: Geschwindigkeit in km/h");

        // ForEach Schleife
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            series.getData().add(new XYChart.Data(k.getReadableTimestamp(), k.getGeschwindigkeit().intValue()));
        }

        this.scene = new Scene(layout,1540,800);
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        lineChart.getData().add(series);

        Hyperlink linkZurueck = new Hyperlink("Zurück");
        linkZurueck.setBorder(Border.EMPTY);
        linkZurueck.setPadding(new Insets(4, 0, 4, 4));
        linkZurueck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindScene().getScene());
            }
        });

        Hyperlink linkUebersicht = new Hyperlink("Zurück zur Übersicht");
        linkUebersicht.setBorder(Border.EMPTY);
        linkUebersicht.setPadding(new Insets(4, 0, 4, 4));
        linkUebersicht.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getOverviewPage().getScene());
            }
        });

        layout.getChildren().addAll(buttonLeiste(), lineChart, linkZurueck,linkUebersicht);

    }

    private Node buttonLeiste() {

        Button aktuell = new Button("Aktuell");
        aktuell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anzeigeFlag = "aktuell";
                lineChart.getData().clear();
                setSeriesToAktuelleDaten();
                lineChart.getData().add(series);
            }
        });

        Button zehntage = new Button("Letzte 10 Tage");
        zehntage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anzeigeFlag = "zehntage";
                lineChart.getData().clear();
                setSeriesToLetzteZehnTageDaten();
                lineChart.getData().setAll(seriesTenDays);
            }
        });
        Button letzterMonat = new Button("Letzter Monat");
        letzterMonat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anzeigeFlag = "letzterMonat";
                lineChart.getData().clear();
                setSeriesToLetzterMonatDaten();
                lineChart.getData().setAll(seriesMonth);
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
        seriesTenDays.getData().add(new XYChart.Data(g.getReadableTimestamp(), g.getGeschwindigkeit().intValue()));
        seriesMonth.getData().add(new XYChart.Data(g.getReadableTimestamp(), g.getGeschwindigkeit().intValue()));
    }

    private void setSeriesToAktuelleDaten() {
        series = new XYChart.Series();
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            series.getData().add(new XYChart.Data(k.getReadableTimestamp(), k.getGeschwindigkeit().intValue()));
        }
    }

    private void setSeriesToLetzteZehnTageDaten() {
        seriesTenDays = new XYChart.Series();
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            Calendar tenDaysAgo = Calendar.getInstance();
            tenDaysAgo.setTime(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)));
            Calendar windTime = Calendar.getInstance();
            windTime.setTime(k.getZeitpunkt());
            if (windTime.after(tenDaysAgo))
            seriesTenDays.getData().add(new XYChart.Data(k.getReadableTimestamp(), k.getGeschwindigkeit().intValue()));
        }
    }

    private void setSeriesToLetzterMonatDaten() {
        seriesMonth = new XYChart.Series();
        for( Windgeschwindigkeit k: daten.getWindGeschwindigkeiten() )
        {
            Calendar lastMonth = Calendar.getInstance();
            lastMonth.setTime(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)));
            Calendar windTime = Calendar.getInstance();
            windTime.setTime(k.getZeitpunkt());
            if (windTime.after(lastMonth))
                seriesMonth.getData().add(new XYChart.Data(k.getReadableTimestamp(), k.getGeschwindigkeit().intValue()));
        }
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
