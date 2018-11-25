package mqtt;

import gui.GUI;
import daten.Daten;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.ActivationMessage;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

public class MqttConnection {
	
	private String region;
	private String appId;
	private String accessKey;
	
	private Client client = null;
	private GUI gui2 = null;
	private Daten daten;
	

	public MqttConnection(String region, String appId, String accessKey, GUI gui, Daten daten) throws Exception {
		this.region = region;
		this.appId = appId;
		this.accessKey = accessKey;
		this.gui2 = gui;
		this.daten = daten;
		createConnection();
	}

	private void createConnection() throws Exception {
		System.out.println("CREATE");
		client = new Client(region, appId, accessKey);
		client.onError((Throwable _error) -> System.err.println("error: " + _error.getMessage() + _error.getLocalizedMessage() + _error.toString()));

		client.onConnected((Connection _client) -> System.out.println("connected !"));
		
		client.onMessage((String devId, DataMessage data) -> doSomethingWithMessage(devId, data));
		client.onActivation((String _devId, ActivationMessage _data) -> System.out.println("Activation: " + _devId + ", data: " + _data));
		client.start();
	}
	
	private Object doSomethingWithMessage(String devId, DataMessage data) {
		UplinkMessage uMe = (UplinkMessage) data;
		System.out.println("Data:" + ((UplinkMessage) data).getPayloadFields());
        try {
            MessageHandler.handleMessage(uMe,daten);
            gui2.updateGui();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return new Object();
	}
}
