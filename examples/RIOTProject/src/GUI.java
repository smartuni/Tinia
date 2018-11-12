import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    private Label label;

    @Override
    public void init(){

    }


    @Override
    public void start(Stage stage) {
        label = new Label("Testlabel");
        initText();

        //Retrieving the observable list object
        ObservableList list = root.getChildren();

        //Setting the text object as a node to the group object
        list.add(text);
        list.add(label);

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
       // label.setText(message);
        label = new Label(message);
        System.out.println("Text updated to: " +  message);

    }


    public void run() {
        launch();
    }

/*    public static void main(String args[]){
        launch(args);
    }*/
}