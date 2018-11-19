package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mqtt.MqttConnection;

public class GUI extends Application {

    private Stage stage;

    private Scene windScene = null;
    private Group rootWind = null;
    private Text windText = null;

    @Override
    public void init() {
        try {
            new MqttConnection("eu", "tini-riot-ws-1819", "ttn-account-v2.1eClE8ktJ5Js0gpZxIX1AifwEEnhDpPJ4ag24jyKdrE", this);
        } catch (Exception e) {
            System.err.println("Fehler in gui.GUI!");
            e.printStackTrace();

        }
    }


    @Override
    public void start(Stage stage) {
        Scene startScene = initStartScene();

        stage.setTitle("Tinia");
        stage.setScene(startScene);

        this.stage = stage;

        //Displaying the contents of the stage
        stage.show();
    }

    private Scene initStartScene() {
        Group root = new Group();
        Scene scene = new Scene(root, 640, 300);
        Text text = new Text();
        //Setting font to the text
        text.setFont(new Font(16));

        //setting the position of the text
        text.setX(50);
        text.setY(150);
        //Setting the text to be added.
        text.setText("Willkommen bei Tinia. Bitte warte während wir die Daten empfangen...");

        //Retrieving the observable list object
        ObservableList list = root.getChildren();


        //Setting the text object as a node to the group object
        list.add(text);

        return scene;

    }

    public void updateText(String message) {
        Platform.runLater(() -> {

            if (windScene == null) {
                rootWind = new Group();
                windScene = new Scene(rootWind, 600, 300);
                stage.setScene(windScene);
                Text mainText = new Text();
                mainText.setText("Tinia - DEIN WINDMESSER");
                mainText.setX(5);
                mainText.setY(30);
                windText = new Text();
                windText.setText("Aktuelle Windgeschwindigkeit: " + message + " km/h");
                windText.setX(5);
                windText.setY(200);

                //Retrieving the observable list object
                ObservableList list = rootWind.getChildren();


                //Setting the text object as a node to the group object
                list.add(mainText);
                list.add(windText);
            }
            windText.setText("Aktuelle Windgeschwindigkeit: " + message + " km/h");
        });
    }


    public void run() {
        launch();
    }

}