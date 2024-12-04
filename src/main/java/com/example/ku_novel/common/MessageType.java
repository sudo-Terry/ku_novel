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

    ROOM_CREATE("ROOM_CREATE"),
    ROOM_CREATE_SUCCESS("ROOM_CREATE_SUCCESS"),
    ROOM_CREATE_FAILED("ROOM_CREATE_FAILED"),
    ROOM_FETCH_BY_ID("ROOM_FETCH_BY_ID"),
    ROOM_FETCH_BY_ID_SUCCESS("ROOM_FETCH_BY_ID_SUCCESS"),
    ROOM_FETCH_BY_ID_FAILED("ROOM_FETCH_BY_ID_FAILED"),
    ROOM_FETCH_BY_TITLE("ROOM_FETCH_BY_TITLE"),
    ROOM_FETCH_BY_TITLE_SUCCESS("ROOM_FETCH_BY_TITLE_SUCCESS"),
    ROOM_FETCH_BY_TITLE_FAILED("ROOM_FETCH_BY_TITLE_FAILED"),
    ROOM_FETCH_ACTIVE("ROOM_FETCH_ACTIVE"),
    ROOM_FETCH_ACTIVE_SUCCESS("ROOM_FETCH_ACTIVE_SUCCESS"),
    ROOM_FETCH_ACTIVE_FAILED("ROOM_FETCH_ACTIVE_FAILED"),
    ROOM_FETCH_ALL("ROOM_FETCH_ALL"),
    ROOM_FETCH_ALL_SUCCESS("ROOM_FETCH_ALL_SUCCESS"),
    ROOM_FETCH_ALL_FAILED("ROOM_FETCH_ALL_FAILED"),
    ROOM_JOIN("ROOM_JOIN"),
    ROOM_LEAVE("ROOM_LEAVE"),
    ROOM_JOIN_SUCCESS("ROOM_JOIN_SUCCESS"),
    ROOM_JOIN_FAILED("ROOM_JOIN_FAILED"),
    ROOM_STATUS_UPDATE("ROOM_STATUS_UPDATE"),
    ROOM_STATUS_UPDATE_SUCCESS("ROOM_STATUS_UPDATE_SUCCESS"),
    ROOM_STATUS_UPDATE_FAILED("ROOM_STATUS_UPDATE_FAILED"),

    AUTHOR_APPLY("AUTHOR_APPLY"),
    AUTHOR_APPLY_RECEIVED("AUTHOR_APPLY_RECEIVED"),
    AUTHOR_APPLY_SUCCESS("AUTHOR_APPLY_SUCCESS"),
    AUTHOR_APPLY_REJECTED("AUTHOR_REJECTED"),
    AUTHOR_WRITE("AUTHOR_WRITE"),
    AUTHOR_WRITE_SUCCESS("AUTHOR_WRITE_SUCCESS"),
    AUTHOR_WRITE_FAILED("AUTHOR_WRITE_FAILED"),
    AUTHOR_APPROVE("AUTHOR_APPROVE"),
    AUTHOR_APPROVED("AUTHOR_APPROVED"),
    AUTHOR_APPROVE_REJECTED("AUTHOR_APPROVE_REJECTED"),
    NOVEL_SUBMITTED("NOVEL_SUBMITTED"),
    ERROR("ERROR"),

    VOTE("VOTE"),
    VOTE_SUCCESS("VOTE_SUCCESS"),
    VOTE_FAILED("VOTE_FAILED"),

    VOTE_FETCH_BY_ID("VOTE_FETCH_BY_ID"), //소설 작성을 누를때 요청해주시면 됩니다!!
    VOTE_FETCH_BY_ID_SUCCESS("VOTE_FETCH_BY_ID_SUCCESS"),
    VOTE_FETCH_BY_ID_FAILED("VOTE_FETCH_BY_ID_FAILED"),

    REFRESH_HOME("REFRESH_HOME"),
    REFRESH_HOME_SUCCESS("REFRESH_HOME_SUCCESS"),
    REFRESH_HOME_FAILED("REFRESH_HOME_FAILED"),

    ATTENDANCE_CHECK("ATTENDANCE_CHECK"),
    ATTENDANCE_CHECK_SUCCESS("ATTENDANCE_CHECK_SUCCESS"),
    ATTENDANCE_CHECK_FAILED("ATTENDANCE_CHECK_FAILED"),
    ROOM_FETCH_DEACTIVATE("ROOM_FETCH_DEACTIVATE"),
    ROOM_FETCH_DEACTIVATE_SUCCESS("ROOM_FETCH_DEACTIVATE_SUCCESS"),
    ROOM_FETCH_DEACTIVATE_FAILED("ROOM_FETCH_DEACTIVATE_FAILED"),
    ROOM_MODIFY_SETTING("ROOM_MODIFY_SETTING"),
    ROOM_MODIFY_SETTING_SUCCESS("ROOM_MODIFY_SETTING_SUCCESS"),
    ROOM_MODIFY_SETTING_FAILED("ROOM_MODIFY_SETTING_FAILED"),

    /// 메시지 관련
    MESSAGE_SEND("MESSAGE_SEND"),
    MESSAGE_RECEIVE("MESSAGE_RECEIVE");

    private String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return messageType;
    }
}