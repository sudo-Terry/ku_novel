package com.example.ku_novel.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;

public class Message {

    private MessageType type;
    private String sender;
    private String receiver;
    private String content;
    private String timestamp;

    // GSON 라이브러리 사용을 위해 빈 생성자가 필요함
    public Message() {}

    public Message(MessageType type, String sender, String receiver, String content) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;

        this.timestamp = Instant.now().toString();
    }

    public Message setType(MessageType type) {
        this.type = type;
        return this;
    }

    public Message setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public Message setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static Message fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Message.class);
    }
}