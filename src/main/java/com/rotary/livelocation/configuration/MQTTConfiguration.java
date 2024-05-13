package com.rotary.livelocation.configuration;

import build.buf.gen.meshtastic.MQTTProtos;
import build.buf.gen.meshtastic.MeshPacket;
import build.buf.gen.meshtastic.ServiceEnvelope;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mapping.ConvertingBytesMessageMapper;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.integration.stream.ByteStreamWritingMessageHandler;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class MQTTConfiguration {
    @Bean
    public MessageChannel mqttInputChannel() {
        log.info("testing");
        return new DirectChannel();
    }

//    @Bean
//    public MessageProducer inbound(MqttPahoClientFactory mqttPahoClientFactory) {
//        MqttPahoMessageDrivenChannelAdapter adapter =
////                new MqttPahoMessageDrivenChannelAdapter("hello2", mqttPahoClientFactory, "msh/ANZCC/#");
//                new MqttPahoMessageDrivenChannelAdapter("hello2", mqttPahoClientFactory, "msh/+/+/map/");
//        adapter.setCompletionTimeout(5000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(0);
//        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }
//
//    @Bean
//    public DefaultMqttPahoClientFactory mqttClientFactory() {
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setServerURIs(new String[]{"tcp://mqtt.meshtastic.org"});
//        options.setUserName("meshdev");
//        options.setPassword("large4cats".toCharArray());
//        options.setCleanSession(true);
//        factory.setConnectionOptions(options);
//        return factory;
//    }


//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")
//    public MessageHandler handler() {
//        return message -> {
//
//            // Extract topic and message payload
//            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
//            log.info("topic: {}", topic);
//            log.info("payload: {}", message.getPayload());
////                log.info("payload: {}", message);
//            try {
//                ServiceEnvelope serviceEnvelope = ServiceEnvelope.parseFrom(message.getPayload());
//                log.info("envelop: {}", serviceEnvelope.getChannelId());
//
//            } catch (InvalidProtocolBufferException ignored) {
//            }
//
//        };
//    }

    public static String toJson(MessageOrBuilder messageOrBuilder) throws IOException {
        return JsonFormat.printer().print(messageOrBuilder);
    }
}
