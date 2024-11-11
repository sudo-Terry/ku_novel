package com.example.ku_novel.common;

public enum MessageType {
    LOGIN("LOGIN"), // 로그인 요청
    LOGOUT("LOGOUT"), // 로그아웃
    LOGIN_SUCCESS("LOGIN_SUCCESS"), // 로그인 성공
    LOGIN_FAILED("LOGIN_FAILED"), // 로그인 실패

    SIGNUP("SIGNUP"), // 회원가입 요청
    CHECK_USERNAME("CHECK_USERNAME"), // 아이디 중복 확인
    CHECK_NICKNAME("CHECK_NICKNAME"), // 닉네임 중복 확인
    SIGNUP_SUCCESS("SIGNUP_SUCCESS"), // 회원가입 성공
    SIGNUP_FAILED("SIGNUP_FAILED");  // 회원가입 실패

    private String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return messageType;
    }
}
