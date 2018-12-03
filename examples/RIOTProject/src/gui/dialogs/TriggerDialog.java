package gui.dialogs;

import daten.Trigger;
import gui.GUI;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;

public class TriggerDialog {
    public TriggerDialog(Trigger trigger, GUI gui, int wert) {
        final Stage dialog = new Stage();
        dialog.setTitle(trigger.getName());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(gui.getStage());
        GridPane dialogBoxLayout = new GridPane();
        dialogBoxLayout.setPadding(new Insets(10, 10, 10, 10));
        dialogBoxLayout.add(new Text("Es wurde ein Trigger ausgelöst!"), 0, 0, 2, 1);
        dialogBoxLayout.add(new Text(""), 0, 1);
        dialogBoxLayout.add(new Text("Triggerbedingung: "), 0, 2);
        dialogBoxLayout.add(new Text("Wert " + trigger.getTriggerRange().getText() + " " + trigger.getValue()), 1, 2);
        dialogBoxLayout.add(new Text("Aktueller Wert:"), 0, 3);
        dialogBoxLayout.add(new Text(String.valueOf(wert)), 1, 3);
        dialogBoxLayout.add(new Text(""), 0, 4);
        dialogBoxLayout.add(new Text("Trigger ausgelöst: " + new Date().toString()), 0, 5, 2, 1);

        Scene dialogScene = new Scene(dialogBoxLayout, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
