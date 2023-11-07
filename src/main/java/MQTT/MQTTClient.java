package MQTT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTClient {
    private MqttClient mqttClient;
    private String mqttBroker ;
    private int qos = 2;

    public MQTTClient(String broker) {
        mqttBroker = broker;
        initialize();
    }

    private void initialize() {
        String clientId = MqttClient.generateClientId();
        try {
            mqttClient = new MqttClient(mqttBroker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishToTopic(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

