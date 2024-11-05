package com.example.ku_novel.client;

import com.example.ku_novel.client.ui.LoginUI;

import javax.swing.*;

public class ClientMain {

    public static void main(String[] args) {
        new ClientMain().showLoginUI();
    }

    public void showLoginUI() {
        SwingUtilities.invokeLater(() -> new LoginUI(this));
    }

    public void startClient(String username) {
        // to-do: 서버 로그인 승인 응답을 받아 메인 페이지에 랜딩 처리
    }
}