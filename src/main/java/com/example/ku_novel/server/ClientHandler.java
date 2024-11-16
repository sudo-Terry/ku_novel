package com.example.ku_novel.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import com.example.ku_novel.common.*;
import com.example.ku_novel.service.UserService;
import com.google.gson.Gson;

class ClientHandler implements Runnable {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String id;

    private final UserService userService;
    private final HashMap<String, PrintWriter> activeClients;

    public ClientHandler(Socket clientSocket, UserService userService, HashMap<String, PrintWriter> activeClients) {
        this.socket = clientSocket;
        this.userService = userService;
        this.activeClients = activeClients;
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
            case SIGNUP:
                handleSignUp(message);
                break;
            case ID_CHECK:
                checkUsername(message);
                break;
            case NICKNAME_CHECK:
                checkNickname(message);
                break;
            // case CHAT:
            // handleChatMessage(messageJson);
            // break;
            // 기타 요청 처리...
        }
    }

    private void handleSignUp(Message message) {
        String userId = message.getSender();
        String password = message.getPassword();
        String nickname = message.getNickname();

        boolean success = userService.registerUser(userId, password, nickname);
        Message responseMessage = new Message();

        if (success) {
            responseMessage.setType(MessageType.SIGNUP_SUCCESS)
                    .setContent("회원가입이 성공했습니다.");
        } else {
            responseMessage.setType(MessageType.SIGNUP_FAILED)
                    .setContent("아이디 또는 닉네임이 이미 존재합니다.");
        }
        sendMessageToClient(responseMessage);
    }

    private void checkUsername(Message message) {
        boolean isDuplicate = userService.isUserIdExists(message.getSender());
        Message responseMessage = new Message()
                .setType(isDuplicate ? MessageType.ID_INVALID : MessageType.ID_VALID)
                .setContent(isDuplicate ? "아이디가 이미 존재합니다." : "사용 가능한 아이디입니다.");
        sendMessageToClient(responseMessage);
    }

    private void checkNickname(Message message) {
        boolean isDuplicate = userService.isNicknameExists(message.getNickname());
        Message responseMessage = new Message()
                .setType(isDuplicate ? MessageType.NICKNAME_INVALID : MessageType.NICKNAME_VALID)
                .setContent(isDuplicate ? "닉네임이 이미 존재합니다." : "사용 가능한 닉네임입니다.");
        sendMessageToClient(responseMessage);
    }

    private void handleLogin(Message message) {

        System.out.println("Login request received.");

        String id = message.getSender();
        String password = message.getPassword();

        // 로그인 비즈니스 로직 처리
        boolean isAuthenticated = userService.validateUserCredentials(id, password);

        Message responseMessage = new Message();
        if (isAuthenticated) {
            this.id = id;
            synchronized (activeClients) {
                activeClients.put(id, out);
            }
            responseMessage.setType(MessageType.LOGIN_SUCCESS);
            responseMessage.setContent("로그인이 성공되었습니다.");
        } else {
            responseMessage.setType(MessageType.LOGIN_FAILED);
            responseMessage.setContent("로그인 실패: 사용자 ID 또는 비밀번호가 잘못되었습니다.");
        }
        // 로그인 성공 시 클라이언트에 응답 전송
        sendMessageToClient(responseMessage);
    }

    private void handleLogout(String messageJson) {
        System.out.println("Logout request received.");
        // 클라이언트 종료 로직
        closeConnection();
    }


    private void handleChatMessage(String messageJson) {
        // 채팅 메시지 처리 로직
        System.out.println("Chat message received.");
        // Broadcast message to all clients
    }

    private void sendMessageToClient(Message message) {
        out.println(message.toJson());
        System.out.println("[SEND] " + message);
    }

    private void closeConnection() {
        try {
            synchronized (activeClients) {
                activeClients.remove(id);
            }
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