package daten;

public class Trigger {
    private String name;
    private String triggerType;
    private TriggerRange triggerRange = TriggerRange.TRIGGER_ABOVE; // ueber / unter
    private int value;
    private boolean active;

    public Trigger(String name, String triggerType, boolean active, TriggerRange triggerRange, int value) {
        this.name = name;
        this.triggerType = triggerType;
        this.active = active;
        this.triggerRange = triggerRange;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getTriggerType() {
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
}
