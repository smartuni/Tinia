package daten;

import java.util.ArrayList;
import java.util.List;

public enum TriggerType {
    MELDUNG("Popup-Meldung"), EMAIL("Versand einer E-Mail"), RUECKGABE("Downlink zum Endgerät");

    private final String label;

    TriggerType(String label) {
        this.label = label;
    }

    public String getText() {
        return label;
    }

    public static List<String> getClearedValues() {
        List<String> l = new ArrayList();
        for (TriggerType r: values()) {
            l.add(r.getText());
        }
        return l;
    }

    public static TriggerType getValue(String label) {
        for (TriggerType r: values()) {
            if(r.getText().equals(label)) {
                return r;
            }
        }
        return null;
    }
}
