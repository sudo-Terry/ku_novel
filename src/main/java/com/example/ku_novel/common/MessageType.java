package com.example.ku_novel.common;

public enum MessageType {
    LOGIN("LOGIN"), // 로그인 요청
    LOGOUT("LOGOUT"), // 로그아웃
    LOGIN_SUCCESS("LOGIN_SUCCESS"), // 로그인 성공
    LOGIN_FAILED("LOGIN_FAILED"), // 로그인 실패

    ID_CHECK("ID_CHECK"), // 아이디 중복 확인
    ID_VALID("ID_VALID"), // 유효한 아이디
    ID_INVALID("ID_INVALID"), // 무효한 아이디

    NICKNAME_CHECK("NICKNAME_CHECK"), // 닉네임 중복 확인
    NICKNAME_VALID("NICKNAME_VALID"), // 유효한 닉네임
    NICKNAME_INVALID("NICKNAME_INVALID"), // 무효한 닉네임

    SIGNUP("SIGNUP"), // 회원가입 요청
    SIGNUP_SUCCESS("SIGNUP_SUCCESS"), // 회원가입 성공
    SIGNUP_FAILED("SIGNUP_FAILED"), // 회원가입 실패

    ROOMS("ROOMS"), // 전체 소설방 조회 요청
    ROOMS_SUCCESS("ROOMS_SUCCESS"), // 전체 소설방 조회 성공
    ROOMS_FAILED("ROOMS_FAILED");

    private String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return messageType;
    }
}