package mqtt;

import daten.Trigger;
import daten.TriggerDataType;
import daten.TriggerRange;
import daten.TriggerType;
import gui.GUI;
import org.thethingsnetwork.data.common.messages.DownlinkMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

public class DownlinkMessageHandler {

    private GUI gui;
    private Client client;
    private String devId;
    private int port = 0;
    private UplinkMessage uplink;

    public DownlinkMessageHandler(Client client, String devId, GUI gui, UplinkMessage uplink) {
        this.client = client;
        this.devId = devId;
        this.gui = gui;
        this.uplink = uplink;

        handleTriggers();
    }

    private void sendDownlinkMessage(String msg) {
        try {
            client.send(devId, new DownlinkMessage(port,msg.getBytes()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTriggers() {
        byte[] uplinkRaw = uplink.getPayloadRaw();
        gui.getTriggerData().forEach((t) -> {
            Trigger trigger = (Trigger) t;
            // Ist der trigger aktiv und soll einen Downlink senden?
            if (trigger.isActive() && trigger.getTriggerType() == TriggerType.RUECKGABE) {
                // Fall: Trigger für Windgeschwindigkeit
                if (trigger.getTriggerDataType() == TriggerDataType.WINDGESCHWINDIGKEIT) {
                    // Durch jeden Channel iterieren um nach Windgeschwindikeit zu suchen
                    int anzahlPorts = uplinkRaw.length/4;
                    int i;
                    for (i = 0; i < anzahlPorts; i++) {
                        // Wenn Windgeschwindigkeit gefunden, prüfen ob Trigger ausgelöst wird
                        if (uplinkRaw[i*4] == 1) {
                            Double wertDouble = (Double) uplink.getPayloadFields().get("analog_out_1");
                            Long wertLong = (Math.round(wertDouble) < 0) ? 0 : Math.round(wertDouble);
                            int wert = wertLong.intValue();
                            // Wenn Trigger asugelöst wird, sende Downlink
                            if (trigger.getTriggerRange() == TriggerRange.TRIGGER_ABOVE) {
                                if (wert > trigger.getValue()) {
                                    sendDownlinkMessage(trigger.getExtraTypeValue());
                                    System.out.println("Downlink gesendet weil Wert zu hoch: " + wert);
                                    break;
                                }
                            } else if(trigger.getTriggerRange() == TriggerRange.TRIGGER_UNDER) {
                                if (wert < trigger.getValue()) {
                                    sendDownlinkMessage(trigger.getExtraTypeValue());
                                    System.out.println("Downlink gesendet weil Wert zu niedrig: " + wert);
                                    break;
                                }
                            }
                        }
                    }

                }
            }
        });
    }

    public Client getClient() {
        return client;
    }

    public String getDevId() {
        return devId;
    }

}
