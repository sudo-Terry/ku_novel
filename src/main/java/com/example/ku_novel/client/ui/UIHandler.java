package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientListenerThread;
import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.common.Message;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UIHandler {
    private static UIHandler instance;
    private Socket socket;
    private PrintWriter writer;
    private ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private LoginUI loginUI;

    // 생성자에서 데몬 스레드 실행
    public UIHandler() {
        try {
            socket = new Socket("127.0.0.1", 10100);
            System.out.println("[Client] Connected to server.");

            // 서버 요청 수신 스레드
            ClientListenerThread clientListenerThread = new ClientListenerThread(socket, messageQueue);
            clientListenerThread.start();

            // 서버 요청 전송 스레드
            ClientSenderThread.initialize(socket);
            ClientSenderThread.getInstance().start();
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

    public static UIHandler getInstance() {
        if (instance == null) {
            instance = new UIHandler();
        }
        return instance;
    }

    public void showLoginUI() {
        SwingUtilities.invokeLater(() -> {
            if (loginUI == null || !loginUI.isVisible()) { // 이미 창이 열려 있지 않을 때만 새로 생성
                loginUI = new LoginUI();
            }
        });
    }

    public void disposeLoginUI() {
        SwingUtilities.invokeLater(() -> {
            if (loginUI != null) { // 로그인 UI가 존재하면 닫기
                loginUI.dispose();
                loginUI = null; // 참조 해제
            }
        });
    }

    public void showSignUpModalUI(JFrame frame) { SwingUtilities.invokeLater(() -> new SignUpModalUI(frame)); }

    public void showAlertModal(Component parentComponent, String title, String message, int messageType) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(parentComponent, message, title, messageType)
        );
    }
}
