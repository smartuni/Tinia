package mqtt;

import daten.Daten;
import daten.Windgeschwindigkeit;
import daten.Windrichtung;
import org.thethingsnetwork.data.common.messages.UplinkMessage;

import java.util.Arrays;
import java.util.Date;

public class MessageHandler {

    public MessageHandler() {}

    public static void handleMessage(UplinkMessage message, Daten daten) throws Exception {

        int[] channels = getChannel(message);
        for( int channel: channels ) {
            switch (channel) {
                case 1:
                    handleWindgeschwindigkeit(message, daten);
                    break;
                case 2:
                    handleMaxWindgeschwindigkeit(message, daten);
                    break;
                case 3:
                    handleWindrichtung(message, daten);
                    break;
                case 4:
                    handleNiederschlag(message, daten);
                    break;
                default:
                    throw new Exception("Channel unbekannt");
            }
        }
    }

    private static int[] getChannel(UplinkMessage message) throws Exception {
        byte[] raw = message.getPayloadRaw();
        int[] channels;
        int rawLength = raw.length;
        switch(rawLength) {
            case 4:
                channels = new int[1];
                channels[0] = message.getPayloadRaw()[0];
                break;
            case 8:
                channels = new int[2];
                channels[0] = message.getPayloadRaw()[0];
                channels[1] = message.getPayloadRaw()[4];
                break;
            case 12:
                channels = new int[3];
                channels[0] = message.getPayloadRaw()[0];
                channels[1] = message.getPayloadRaw()[4];
                channels[2] = message.getPayloadRaw()[8];
                break;
            case 16:
                channels = new int[4];
                channels[0] = message.getPayloadRaw()[0];
                channels[1] = message.getPayloadRaw()[4];
                channels[2] = message.getPayloadRaw()[8];
                channels[3] = message.getPayloadRaw()[12];
                break;
            default: throw new Exception("Payload kann nicht verarbeitet werden: " + Arrays.toString(message.getPayloadRaw()));
        }
        return channels;
    }

    private static void handleWindgeschwindigkeit(UplinkMessage message, Daten daten) {

        Double wert = ((Double) message.getPayloadFields().get("analog_out_1"));
        Long ganzZahlWert = (Math.round(wert) < 0) ? 0 : Math.round(wert);
        System.out.println("Windgeschwindigkeit erhalten: " + ganzZahlWert);
        daten.addWindgeschwindigkeit(new Windgeschwindigkeit(ganzZahlWert.intValue(), new Date()));


    }

    private static void handleMaxWindgeschwindigkeit(UplinkMessage message, Daten daten) {

        Double wert = ((Double) message.getPayloadFields().get("analog_out_2"));
        System.out.println("Maximale Windgeschwindigkeit erhalten: " + wert);

    }

    private static void handleWindrichtung(UplinkMessage message, Daten daten) {

       // Double wert = ((Double) message.getPayloadFields().get("analog_out_3"));
       // Long ganzZahlWert = (Math.round(wert) < 0) ? 0 : Math.round(wert);
        Integer wert = ((Integer) message.getPayloadFields().get("analog_out_3"));
        System.out.println("Windrichtungswert erhalten: " + wert);
        daten.addWindrichtung(new Windrichtung(wert,new Date()));

    }

    private static void handleNiederschlag(UplinkMessage message, Daten daten) {

        Double wert = ((Double) message.getPayloadFields().get("analog_out_4"));
        System.out.println("Niederschlagswert erhalten: " + wert);
    }

}
