package com.example.demo.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Value("${kafka.event-push.topic}")
    private String kafkaEventPushTopic;
    @Value("${stomp.event-push-topic}")
    private String stompEventPushTopic;

    private final KafkaTemplate<Object, EventPush<EventPushContent>> kafkaTemplate;

    public ApiController(KafkaTemplate<Object, EventPush<EventPushContent>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("test-push")
    public ResponseEntity<String> test(@RequestBody String msg) {
        var eventpush = new EventPush<EventPushContent>();
        eventpush.setStompTopic(String.format(stompEventPushTopic,"spin"));
        var eventPushContent = new EventPushContent();
        eventPushContent.setMsg(msg);
        eventpush.setContent(eventPushContent);
        kafkaTemplate.send(kafkaEventPushTopic, eventpush);
        return ResponseEntity.ok().body(msg);
    }

}
