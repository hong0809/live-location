package com.rotary.livelocation.configuration;

import build.buf.gen.meshtastic.Data;
import build.buf.gen.meshtastic.MeshPacket;
import build.buf.gen.meshtastic.ServiceEnvelope;
import com.rotary.livelocation.mqtt.MQTTClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UTFDataFormatException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.BiConsumer;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Configuration
@Slf4j
public class MQTTSubscriber {
    private final MQTTClient mqttClient;

    @Autowired
    public MQTTSubscriber(MQTTClient mqttClient) {
        this.mqttClient = mqttClient;
    }


    @PostConstruct
    public void subscribeToTopic() {
        String topic = "msh/+/+/LongFast/+"; // Specify the MQTT topic you want to subscribe to

        try {
            // Subscribe to the topic using the MQTTClient
            mqttClient.subscribe(topic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    log.info("topic {}", s);
                    log.info("mqtt message {}:", new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
                    ServiceEnvelope envelope = ServiceEnvelope.parseFrom(mqttMessage.getPayload());
                    log.info("envelope ={}", envelope.getPacket());
                    decrypt(envelope.getPacket());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String decrypt(MeshPacket packet) throws Exception {


        SecretKeySpec key = new SecretKeySpec(getKey(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(getNonce(packet));

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] original = cipher.doFinal(packet.getEncrypted().toByteArray());

        String plaintext = new String(original, StandardCharsets.UTF_8);
        System.out.println("plaintext: " + plaintext);

        try {
            Data data = Data.parseFrom(original);
            log.info("data: {} , data= {}, original= {}", data.getPayload(), data, original);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return plaintext;

    }

    public static byte[] getKey() {
        return Base64.getDecoder().decode("1PG7OiApB1nwvP+rz05pAQ==");
    }

    public static byte[] getNonce(MeshPacket packet) {
        long l = (long) packet.getId();
        ByteBuffer buffer = ByteBuffer.allocate(16);

        buffer.putLong(l);
//        buffer.putInt(0);
        buffer.putInt(packet.getFrom());
        buffer.putInt(0);
        return buffer.array();
    }

}
