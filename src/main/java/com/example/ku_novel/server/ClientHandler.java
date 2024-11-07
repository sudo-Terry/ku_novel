package com.example.ku_novel.server;
import java.io.*;
import java.net.*;

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket) {
        this.socket = clientSocket;
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

    private void processClientRequest(String messageJson) {
        // JSON 파싱하여 MessageType 분기 처리
        // MessageType에 따라 비즈니스 로직 호출
        // MessageType type = parseMessageType(messageJson);

        // switch (type) {
        //     case LOGIN_REQUEST:
        //         handleLogin(messageJson);
        //         break;
        //     case CHAT_MESSAGE:
        //         handleChatMessage(messageJson);
        //         break;
        //     case LOGOUT_REQUEST:
        //         handleLogout(messageJson);
        //         break;
        //     // 기타 요청 처리...
        // }
    }

    private void handleLogin(String messageJson) {
        // 로그인 비즈니스 로직 처리
        System.out.println("Login request received.");
        // 로그인 성공 시 클라이언트에 응답 전송
        sendMessageToClient("{\"type\":\"LOGIN_RESPONSE\", \"status\":\"SUCCESS\"}");
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

    private void sendMessageToClient(String responseJson) {
        out.println(responseJson);
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
