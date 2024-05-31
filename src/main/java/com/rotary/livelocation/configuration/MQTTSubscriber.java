package com.rotary.livelocation.configuration;

import build.buf.gen.meshtastic.*;
import com.rotary.livelocation.mqtt.MQTTClient;
import com.rotary.livelocation.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import java.util.Base64;

@Configuration
@Slf4j
public class MQTTSubscriber {
    private final MQTTClient mqttClient;

    @Autowired
    PositionService positionService;

    @Autowired
    public MQTTSubscriber(MQTTClient mqttClient) {
        this.mqttClient = mqttClient;
    }


//    @PostConstruct
    public void subscribeToTopic() {
        String topic = "msh/ANZCC/#"; // Specify the MQTT topic you want to subscribe to
        try {
            // Subscribe to the topic using the MQTTClient
            mqttClient.subscribe(topic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    if (s.contains("/stat")) {
                        return;
                    }
                    log.info("topic {}", s);
//                    log.info("mqtt message {}:", new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
                    ServiceEnvelope envelope = ServiceEnvelope.parseFrom(mqttMessage.getPayload());
                    log.info("envelope = {}", envelope.getPacket().getDecoded().getPortnum().toString());
                    try {
//                        decrypt(envelope.getPacket());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try{
                        if (envelope.getPacket().getDecoded().getPortnum().name().equals("POSITION_APP")) {

                            Position position = Position.parseFrom(envelope.getPacket().getDecoded().getPayload());
                            log.info("position = {}", position);
                        } else if (envelope.getPacket().getDecoded().getPortnum().name().equals("MAP_REPORT_APP")) {
                            MapReport map = MapReport.parseFrom(envelope.getPacket().getDecoded().getPayload());
                            log.info("map = {}", map);
                        }

                        Data datax = Data.parseFrom(envelope.getPacket().getDecoded().getPayload());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
        buffer.putInt(packet.getFrom());
        buffer.putInt(0);
        return buffer.array();
    }


}
