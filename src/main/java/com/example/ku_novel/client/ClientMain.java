package com.example.ku_novel.client;

import com.example.ku_novel.client.connection.ClientListenerThread;
import com.example.ku_novel.client.ui.LoginUI;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class ClientMain {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 10100);
            new ClientListenerThread(socket).run();

            new ClientMain().showLoginUI();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void showLoginUI() {
        SwingUtilities.invokeLater(() -> new LoginUI(this));
    }

    public void startClient(String username) {
        // to-do: 서버 로그인 승인 응답을 받아 메인 페이지에 랜딩 처리
    }
}