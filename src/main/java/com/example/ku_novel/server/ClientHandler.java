package com.example.ku_novel.server;

import java.io.*;
import java.net.*;

import com.example.ku_novel.common.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private DatabaseManager dbManager;

    public ClientHandler(Socket clientSocket, DatabaseManager dbManager) {
        this.socket = clientSocket;
        this.dbManager = dbManager;
        try {
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String receivedMessage;
            while ((receivedMessage = in.readLine()) != null) {
                processClientRequest(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private Message parseMessage(String messageJson) {
        Gson gson = new Gson();
        return gson.fromJson(messageJson, Message.class);
    }

    private void processClientRequest(String messageJson) {
        // JSON 파싱하여 MessageType 분기 처리
        // MessageType에 따라 비즈니스 로직 호출
        Message message = parseMessage(messageJson);
        System.out.println("[RECEIVE] " + message);

        switch (message.getType()) {
            case LOGIN:
                handleLogin(message);
                break;
            case LOGOUT:
                handleLogout(messageJson);
                break;
            case LOGIN_FAILED:
                handleLogout(messageJson);
                break;
            case LOGIN_SUCCESS:
                handleLogout(messageJson);
                break;
            // case CHAT:
            // handleChatMessage(messageJson);
            // break;
            // 기타 요청 처리...
        }
    }

    private void handleLogin(Message message) {
        // 로그인 비즈니스 로직 처리
        System.out.println("Login request received.");

        Message responseMessage = new Message();
        responseMessage.setType(MessageType.LOGIN_SUCCESS);
        responseMessage.setContent("로그인이 성공되었습니다.");

        // 로그인 성공 시 클라이언트에 응답 전송
        sendMessageToClient(responseMessage);
    }

    private void handleChatMessage(String messageJson) {
        // 채팅 메시지 처리 로직
        System.out.println("Chat message received.");
        // Broadcast message to all clients
    }

    private void handleLogout(String messageJson) {
        System.out.println("Logout request received.");
        // 클라이언트 종료 로직
    }

    private void sendMessageToClient(Message message) {
        out.println(message.toJson());
        System.out.println("[SEND] " + message);
    }

    private void closeConnection() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
