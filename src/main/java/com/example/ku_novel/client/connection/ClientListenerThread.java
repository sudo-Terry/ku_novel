package com.example.ku_novel.client.connection;

import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.HomeUI;
import com.example.ku_novel.client.ui.UIHandler;
import com.example.ku_novel.common.Message;
import com.example.ku_novel.common.MessageType;
import com.example.ku_novel.domain.NovelRoom;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientListenerThread extends Thread {
    private final Socket socket;
    private final ConcurrentLinkedQueue<Message> messageQueue;
    private final Gson gson;

    public ClientListenerThread(Socket socket, ConcurrentLinkedQueue<Message> messageQueue) {
        this.socket = socket;
        this.messageQueue = messageQueue;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            UIHandler uiHandler = UIHandler.getInstance();
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line + " 응답 도착");

                JsonObject jsonObject = gson.fromJson(line, JsonObject.class);
                String messageType = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "";

                if (messageType.isEmpty()) {
                    throw new IllegalArgumentException("메시지 타입이 없습니다.");
                }

                handleMessageType(messageType, jsonObject, uiHandler);
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private void handleMessageType(String messageType, JsonObject jsonObject, UIHandler uiHandler) {
        switch (messageType) {
            case "LOGIN_SUCCESS" -> handleLoginSuccess(jsonObject, uiHandler);
            case "LOGIN_FAILED", "SIGNUP_FAILED" -> uiHandler.showAlertModal(
                    null, "경고", jsonObject.get("content").getAsString(), JOptionPane.ERROR_MESSAGE);
            case "ID_INVALID", "ID_VALID", "NICKNAME_INVALID", "NICKNAME_VALID" -> uiHandler.showAlertModal(
                    null, "정보", jsonObject.get("content").getAsString(), JOptionPane.INFORMATION_MESSAGE);
            case "SIGNUP_SUCCESS" -> handleSignupSuccess(jsonObject, uiHandler);
            case "ROOM_FETCH_BY_TITLE_SUCCESS" -> handleRoomFetchByTitleSuccess(jsonObject, uiHandler);
            case "ROOM_FETCH_BY_TITLE_FAILED" -> handleRoomFetchByTitleFailed(jsonObject, uiHandler);
            default -> enqueueMessage(jsonObject);
        }
    }

    private void handleLoginSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        System.out.println("로그인 성공");

        ClientDataModel dataModel = ClientDataModel.getInstance();

        dataModel.setUserId(jsonObject.get("sender").getAsString());
        dataModel.setPassword(jsonObject.get("password").getAsString());
        dataModel.setUserName(jsonObject.get("nickname").getAsString());
        dataModel.setUserPoint(jsonObject.get("point").getAsString());
        dataModel.setChatRoomsFromJson(jsonObject);

        new HomeUI();
        uiHandler.disposeLoginUI();
    }

    private void handleSignupSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        uiHandler.disposeSignUpModalUI();
        uiHandler.showAlertModal(null, "정보", jsonObject.get("content").getAsString(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleRoomFetchByTitleSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        String content = jsonObject.get("content").getAsString();
        NovelRoom[] rooms = gson.fromJson(content, NovelRoom[].class);

        // TODO : UI에 검색 결과 표시 (UIHandler에 구현 필요)
        uiHandler.showRoomSearchResultsModal(rooms);
    }

    private void handleRoomFetchByTitleFailed(JsonObject jsonObject, UIHandler uiHandler) {
        String errorMessage = jsonObject.get("content").getAsString();
        uiHandler.showAlertModal(null, "오류", "방 검색 실패: " + errorMessage, JOptionPane.ERROR_MESSAGE);
        System.out.println("검색 실패: " + errorMessage);
    }

    private void enqueueMessage(JsonObject jsonObject) {
        Message message = gson.fromJson(jsonObject, Message.class);
        messageQueue.add(message);
        System.out.println(message + " 가 큐에 추가됨");
    }

    private void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("소켓 닫기 오류: " + e.getMessage());
        }
    }
}