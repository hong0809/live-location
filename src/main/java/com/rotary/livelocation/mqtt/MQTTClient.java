package com.rotary.livelocation.mqtt;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * Client (wraps sign-in and paho library) for talking to our MQTT broker
 *
 * test.mosquitto.org
 *
 * Note: Most subscribers of MQTTClient must have their own instance, because the paho subscription code only allows one handler per unique topic string
 */
@Configuration
public class MQTTClient extends MqttClient {
    /**
     * 0 – “at most once” semantics, also known as “fire-and-forget”. Use this option when message loss is acceptable, as it does not require any kind of acknowledgment or persistence
     * 1 – “at least once” semantics. Use this option when message loss is not acceptable and your subscribers can handle duplicates
     * 2 – “exactly once” semantics. Use this option when message loss is not acceptable and your subscribers cannot handle duplicates
     */
    private final int defaultQOS = 1;

    public MQTTClient() throws MqttException {
        super("tcp://mqtt.meshtastic.org", generateClientId(), null);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("meshdev");
        options.setPassword("large4cats".toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(false); // Must be false to autoresubscribe on reconnect
        options.setConnectionTimeout(10);
        connect(options);
    }

    public void publish(String topic, byte[] msg) throws MqttException {
        publish(topic, msg, defaultQOS, false);
    }

    // Generate a random client ID
    public static String generateClientId() {
        return UUID.randomUUID().toString();
    }
}