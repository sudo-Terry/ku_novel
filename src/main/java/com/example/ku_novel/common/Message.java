package com.example.ku_novel.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class Message {

    // Getters
    @Getter
    private MessageType type;
    @Getter
    private String sender;
    private String receiver;
    private String content;
    private String timestamp;
    private String password;
    private String nickname;

    private String json;

    private int votingDuration;
    private int submissionDuration;
    private List<Message> novelRooms;
    private Long novelRoomId;
    private String novelRoomTitle;
    private String novelRoomDescription;
    private String novelRoomStatus;
    private int maxParticipants;
    private boolean isParticipating;

    // GSON 라이브러리 사용을 위해 빈 생성자가 필요함
    public Message() {
    }

    public Message(MessageType type, String sender, String receiver, String content, String password, String nickname) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.password = password;
        this.nickname = nickname;
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

    public Message setJson(String json) {
        this.json = json;
        return this;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public Message setPassword(String password) {
        this.password = password;
        return this;
    }

    public Message setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public static Message fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Message.class);
    }
}