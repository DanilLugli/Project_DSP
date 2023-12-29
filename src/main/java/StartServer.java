import beans.Pollution;
import beans.PollutionModels;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import MQTT.MQTTClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartServer {


    private static final String HOST = "localhost";
    private static final int PORT = 1993;

    private static MqttClient mqttSubscriber;


    public static void main(String[] args) throws IOException {
        Logger.getLogger( "com" ).setLevel(Level.SEVERE);
        HttpServer server = HttpServerFactory.create("http://" +HOST+":"+PORT+"/");
        server.start();

        System.out.println("Server running!");
        System.out.println("Server started on: http://"+HOST+":"+PORT);

        String mqttBroker = "tcp://localhost:1883"; // Change to your MQTT broker address
        MQTTClient mqttPublisher = new MQTTClient(mqttBroker);

        // Subscribe to the "district" MQTT topic
        subscribeToTopic("greenfield/pollution/district/0");
        subscribeToTopic("greenfield/pollution/district/1");
        subscribeToTopic("greenfield/pollution/district/2");
        subscribeToTopic("greenfield/pollution/district/3");


        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");

    }

    private static void subscribeToTopic(String topic) {
        try {
            String clientId = MqttClient.generateClientId();
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            mqttSubscriber = new MqttClient("tcp://localhost:1883", clientId, new MemoryPersistence());
            mqttSubscriber.connect(connOpts);

            mqttSubscriber.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to MQTT broker lost!");
                }

                @Override
                public synchronized void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());

                    String[] parts = payload.split(",");

                    String rID = parts[0].trim();
                    double pm_10 = Double.parseDouble(parts[1].trim());
                    long timestamp = Long.parseLong(parts[2].trim());

                    Pollution p = new Pollution(pm_10, timestamp);
                    PollutionModels.getInstance().addPollution(rID, p);
                    System.out.println("Received message on topic '" + topic + "': " + payload);

                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used for subscribers
                }
            });

            mqttSubscriber.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}

