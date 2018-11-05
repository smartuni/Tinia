import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.mqtt.Client;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.common.messages.ActivationMessage;

public class Start {
	public static void main(String[] args){
		try {
			new MqttConnection("eu", "tini-riot-ws-1819", "ttn-account-v2.1eClE8ktJ5Js0gpZxIX1AifwEEnhDpPJ4ag24jyKdrE");
		} catch (Exception e) {
			System.err.println("Fehler!");
		}
	}
}
