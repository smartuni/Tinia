package daten;

/**
 * Trigger, der ein Event auslöst wenn eine bestimmte Bedingung erfüllt ist
 */
public class Trigger {
    private String name;
    private TriggerType triggerType;
    private TriggerRange triggerRange = TriggerRange.TRIGGER_ABOVE; // ueber / unter
    private TriggerDataType triggerDataType;
    private int value;
    private boolean active;

    public Trigger(String name, TriggerType triggerType, boolean active, TriggerRange triggerRange, TriggerDataType triggerDataType, int value) {
        this.name = name;
        this.triggerType = triggerType;
        this.active = active;
        this.triggerRange = triggerRange;
        this.triggerDataType = triggerDataType;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public boolean isActive() {
        return active;
    }

    public TriggerRange getTriggerRange() {
        return triggerRange;
    }

    public void setTriggerRange(TriggerRange triggerRange) {
        this.triggerRange = triggerRange;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTriggerRangeReadable() {
        return this.getTriggerRange().getText();
    }

    public TriggerDataType getTriggerDataType() {
        return triggerDataType;
    }

    public void setTriggerDataType(TriggerDataType triggerDataType) {
        this.triggerDataType = triggerDataType;
    }

}
