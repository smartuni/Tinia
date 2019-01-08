package gui.scenes;

import daten.Daten;
import gui.GUI;
import gui.GUIUtils;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class OverviewPage implements GUIScene {
    private Scene scene;

    final JFXPanel fxPanel = new JFXPanel();
    private Daten daten;

    private GUI gui;

    public OverviewPage(Daten daten, GUI gui) {
        this.gui = gui;
        this.daten = daten;
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 20, 10, 20));
        this.scene = new Scene(layout,1540,800);
        layout.setId("pane");
        this.scene.getStylesheets().addAll(this.getClass().getResource("/stage.css").toExternalForm());
        layout.setTop(GUIUtils.createFancyHeadline("Übersicht"));
        layout.setCenter(overviewAllData());
        layout.setBottom(bottomLinkBar());
        gui.getWindScene().updateText();
        gui.getWindRichtungScene().updateText();

    }

    private Node overviewAllData() {

        //Creating a Grid Pane
        GridPane gridPane = new GridPane();
        gridPane.setId("overViewPane");

        //Setting size for the pane
        gridPane.setMinSize(400, 200);

        //Setting the padding
      //  gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(new Text("Wind:"), 0, 0);
        gridPane.add(new Text(daten.getWindGeschwindigkeiten().get(daten.getWindGeschwindigkeiten().size() - 1).getGeschwindigkeit() + " km / h"), 2, 0);
        gridPane.add(new Text("Windrichtung:"), 0, 1);
        gridPane.add(new Text(daten.getWindrichtungen().get(daten.getWindrichtungen().size() - 1).getReadableWindrichtung()), 2, 1);
        //gridPane.add(new Text(""), 1, 1);
        gridPane.add(new Text("Niederschlag:"), 0, 2);
        gridPane.add(new Text("60 mm / m²"), 2, 2);

        return gridPane;
    }

    private Node bottomLinkBar() {
        HBox bottomBar = new HBox(10);

        Hyperlink windLink = new Hyperlink("Wind");
        windLink.setBorder(Border.EMPTY);
        windLink.setPadding(new Insets(0, 0, 50, 15));
        windLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindScene().getScene());
            }
        });

        Hyperlink windDirectionLink = new Hyperlink("Wind-Richtung");
        windDirectionLink.setBorder(Border.EMPTY);
        windDirectionLink.setPadding(new Insets(0, 0, 50, 15));
        windDirectionLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindRichtungScene().getScene());
            }
        });

        Hyperlink rainfallLink = new Hyperlink("Niederschlag");
        rainfallLink.setBorder(Border.EMPTY);
        rainfallLink.setPadding(new Insets(0, 0, 50, 15));
        /*overviewLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gui.getStage().setScene(gui.getWindScene().getScene());
            }
        });*/

        bottomBar.getChildren().addAll(windLink, windDirectionLink, rainfallLink);
        bottomBar.setAlignment(Pos.BOTTOM_CENTER);
        return bottomBar;
    }


    @Override
    public Scene getScene() {
        return this.scene;
    }

}
