import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GUI extends Application {

    //Creating a Text object
    private Text text = new Text();

    //Creating a Group object
    private Group root = new Group();

    //Creating a scene object
    private Scene scene = new Scene(root, 600, 300);

    @Override
    public void init(){

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
        text.setFont(new Font(45));

        //setting the position of the text
        text.setX(50);
        text.setY(150);
        //Setting the text to be added.
        text.setText("Welcome to Tinia");
    }

    public void updateText(String message) {
        //text.setText(message);
        System.out.println("Text updated to: " +  message);

        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String text, String message) {

                System.out.println(" Text Changed to  " + message + ")\n");
            }
        });


    }


    public void run() {
        launch();
    }

/*    public static void main(String args[]){
        launch(args);
    }*/
}