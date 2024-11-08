package com.example.ku_novel.common;

public enum MessageType {
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT"),
    LOGIN_SUCCESS("LOGIN_SUCCESS"),
    LOGIN_FAILED("LOGIN_FAILED");

    private String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    public String toString() {
        return messageType;
    }
}
