package com.example.demo.websocket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

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

    @KafkaListener(topics = "${kafka.event-push.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage2(String evenPushJson) {

        Gson gson = new Gson();
        Type type = new TypeToken<EventPush<EventPushContent>>(){}.getType();
        EventPush<EventPushContent> eventPushJson = gson.fromJson(evenPushJson, type);

        System.out.println("Message stomp topic: " + eventPushJson.getStompTopic());
        System.out.println("Message stomp topic: " + eventPushJson.getContent().getMsg());
        messagingTemplate.convertAndSend(eventPushJson.getStompTopic(), eventPushJson.getContent().getMsg());
    }

}