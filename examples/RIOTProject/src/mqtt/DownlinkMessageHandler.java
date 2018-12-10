package mqtt;

import org.thethingsnetwork.data.mqtt.Client;

public class DownlinkMessageHandler {

    private Client client;
    private String devId;

    public DownlinkMessageHandler(Client client, String devId) {
        this.client = client;
        this.devId = devId;
    }

    public void sendDownlinkMessage(String msg, int port) {

    }

    public Client getClient() {
        return client;
    }

    public String getDevId() {
        return devId;
    }
}
