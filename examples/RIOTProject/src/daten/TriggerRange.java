package daten;

import java.util.ArrayList;
import java.util.List;

public enum TriggerRange {
    TRIGGER_ABOVE("über"), TRIGGER_UNDER("unter");

    private final String label;

    TriggerRange(String label) {
        this.label = label;
    }

    public String getText() {
        return label;
    }

    public static List<String> getClearedValues() {
        List<String> l = new ArrayList();
        for (TriggerRange r: values()) {
            l.add(r.getText());
        }
        return l;
    }

    public static TriggerRange getValue(String label) {
        for (TriggerRange r: values()) {
            if(r.getText().equals(label)) {
                return r;
            }
        }
        return null;
    }
}
