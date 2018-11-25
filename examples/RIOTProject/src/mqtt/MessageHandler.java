package mqtt;

import daten.Daten;
import daten.Windgeschwindigkeit;
import org.thethingsnetwork.data.common.messages.UplinkMessage;

import java.util.Date;

public class MessageHandler {

    public MessageHandler() {}

    public static void handleMessage(UplinkMessage message, Daten daten) throws Exception {

        int channel = getChannel(message);
        switch(channel) {
            case 2:
                handleWindgeschwindigkeit(message, daten);
                break;
            default: throw new Exception("Port unbekannt");
        }
    }

    private static int getChannel(UplinkMessage message) {
        byte[] raw = message.getPayloadRaw();
        if (raw.length == 4) {
            return message.getPayloadRaw()[0];
        } else {
            return -1;
        }
    }

    private static void handleWindgeschwindigkeit(UplinkMessage message, Daten daten) {

        Double wert = ((Double) message.getPayloadFields().get("analog_out_2"));
        Long ganzZahlWert = Math.round(wert);

        daten.addWindgeschwindigkeit(new Windgeschwindigkeit(ganzZahlWert.intValue(), new Date()));

    }

}
