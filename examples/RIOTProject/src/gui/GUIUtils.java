package gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GUIUtils {
    private GUIUtils() { }

    public static Node createFancyHeadline(String labelText) {
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 0, 0, 10));
        vb.setSpacing(10);
        Text text = new Text(labelText);
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        text.setStyle("-fx-margin: 10px");
        vb.getChildren().add(text);
        return vb;
    }
}
