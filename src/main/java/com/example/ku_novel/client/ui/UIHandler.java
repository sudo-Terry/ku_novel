package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientListenerThread;
import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.common.Message;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UIHandler {
    private Socket socket;
    private PrintWriter writer;
    private ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private ClientSenderThread clientSenderThread;

    // 생성자에서 데몬 스레드 실행
    public UIHandler() {
        try {
            socket = new Socket("127.0.0.1", 10100);
            System.out.println("[Client] Connected to server.");

            // 서버 요청 수신 스레드
            ClientListenerThread clientListenerThread = new ClientListenerThread(socket, messageQueue);
            clientListenerThread.start();

            // 서버 요청 전송 스레드
            clientSenderThread = new ClientSenderThread(socket);
            clientSenderThread.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }/*finally {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }*/
    }

    public void showLoginUI() {
        SwingUtilities.invokeLater(() -> new LoginUI(clientSenderThread));
    }
}
