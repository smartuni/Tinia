package daten;

public enum TriggerRange {
    TRIGGER_ABOVE("über"), TRIGGER_UNDER("unter");

    private final String label;

    TriggerRange(String label) {
        this.label = label;
    }

    public String getText() {
        return label;
    }
}
