import org.eclipse.paho.client.mqttv3.MqttException;
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
	private MonitorFrame gui = null;
	private GUI gui2 = null;
	

	public MqttConnection(String region, String appId, String accessKey) throws MqttException, Exception {
		this.region = region;
		this.appId = appId;
		this.accessKey = accessKey;
		//gui = new MonitorFrame();
		// JavaFX Fenster
		gui2 = new GUI();
		gui2.run();
		createConnection();

	}

	private void createConnection() throws MqttException, Exception {
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
		Double wert = ((Double) uMe.getPayloadFields().get("analog_out_2"));
		Long ganzZahlWert = Math.round(wert);
		//gui.updateSpeed(ganzZahlWert.intValue());
		gui2.updateText(ganzZahlWert.toString());
		return new Object();
	}
}
