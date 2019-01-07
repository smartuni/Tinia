package gui.scenes;

import daten.*;
import gui.GUI;
import gui.dialogs.TriggerDialog;
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

import java.util.Date;


public class WindRichtungScene implements GUIScene {
    private Scene scene;

    final JFXPanel fxPanel = new JFXPanel();

    private Text messungText = new Text();


    private Daten daten;

    private GUI gui;

    public WindRichtungScene(Daten daten, GUI gui) {
        this.gui = gui;
        this.daten = daten;
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 20, 10, 20));
        this.scene = new Scene(layout,1540,800);
        layout.setId("pane");
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.setTop(initHeadline());
        layout.setCenter(initMessung());
        layout.setBottom(initMinMaxValue());
    }

    private Node initMinMaxValue() {

        Hyperlink chartLink = new Hyperlink("Historische Windrichtungen");
        chartLink.setBorder(Border.EMPTY);
        chartLink.setPadding(new Insets(0, 5, 4, 5));
        chartLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindrichtungTableScene().getScene());
            }
        });

        Hyperlink linkZurueck = new Hyperlink("Zurück");
        linkZurueck.setBorder(Border.EMPTY);
        linkZurueck.setPadding(new Insets(0, 5, 4, 5));
        linkZurueck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getOverviewPage().getScene());
            }
        });


        HBox valueLayout = new HBox();
        valueLayout.setSpacing(10);
        valueLayout.getChildren().addAll(chartLink,linkZurueck);
        return valueLayout;
    }

    private Text initHeadline() {
        Text text = new Text();
        text.setId("windTextHeadline");
        text.setText("Tinia - DER Windmesser");
        return text;
    }

    private Text initMessung() {
        messungText.setId("windRichtungMessungText");
        return messungText;
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }


    public void updateText() {
        String wert = daten.getWindrichtungen().get(daten.getWindrichtungen().size() - 1).getReadableWindrichtung();
        messungText.setText("Aktuelle Windrichtung: " + wert);
        gui.getWindrichtungTableScene().updateTable();
    }
}
