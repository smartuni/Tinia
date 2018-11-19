package mqtt;

import org.thethingsnetwork.data.common.messages.UplinkMessage;

public class MessageHandler {

    public MessageHandler() {}

    public void handleMessage(UplinkMessage message) throws Exception {

        String port = (String)message.getPayloadFields().get("port");

        switch(port) {
            case "2":
                handleWindgeschwindigkeit(message);
                break;
            default: throw new Exception("Port unbekannt");
        }
    }

    private void handleWindgeschwindigkeit(UplinkMessage message) {

    }

}
