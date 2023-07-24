package com.example.demo.websocket;

import lombok.Data;

@Data
public class EventPush<T> {

    private String stompTopic;

    private T content;

}
