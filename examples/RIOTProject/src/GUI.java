import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    //Creating a Text object
    private Text text = new Text();

    //Creating a Group object
    private Group root = new Group();

    //Creating a scene object
    private Scene scene = new Scene(root, 600, 300);


    @Override
    public void init() {
        try {
            new MqttConnection("eu", "tini-riot-ws-1819", "ttn-account-v2.1eClE8ktJ5Js0gpZxIX1AifwEEnhDpPJ4ag24jyKdrE", this);
        } catch (Exception e) {
            System.err.println("Fehler in GUI!");
            e.printStackTrace();

        }
    }


    @Override
    public void start(Stage stage) {

        initText();

        //Retrieving the observable list object
        ObservableList list = root.getChildren();


        //Setting the text object as a node to the group object
        list.add(text);


        initStage(stage);

        //Displaying the contents of the stage
        stage.show();
    }


    private void initStage(Stage stage) {

        //Setting title to the Stage
        stage.setTitle("Tinia");

        //Adding scene to the stage
        stage.setScene(scene);
    }

    private void initText() {
        //Setting font to the text
        text.setFont(new Font(20));

        //setting the position of the text
        text.setX(50);
        text.setY(150);
        //Setting the text to be added.
        text.setText("Welcome to Tinia");


    }

    public void updateText(String message) {

        text.setText("Aktuelle Windgeschwindigkeit: " + message + " km/h");
    }


    public void run() {
        launch();
    }

}