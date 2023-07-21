package com.example.demo.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @Value("${stomp.topic}")
    private String stompTopic;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${kafka.output.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(String msg) {
        System.out.println("Message received: " + msg);
        messagingTemplate.convertAndSend(stompTopic, msg);
    }

}