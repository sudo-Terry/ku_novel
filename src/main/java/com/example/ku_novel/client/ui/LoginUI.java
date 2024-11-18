package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {

    public LoginUI() {

        setTitle("로그인");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        Dimension fieldSize = new Dimension(250, 30);

        // to-do : 이미지 디자인하여 추가
        ImageIcon icon = new ImageIcon("src/main/resources/sample.jpg");
        JLabel imageLabel = new JLabel(icon);

        JLabel userLabel = new JLabel("아이디:");
        JTextField userText = new JTextField();
        userText.setPreferredSize(fieldSize);

        JLabel passwordLabel = new JLabel("비밀번호:");
        JPasswordField passwordText = new JPasswordField();
        passwordText.setPreferredSize(fieldSize);

        JButton loginButton = new JButton("로그인");
        loginButton.setPreferredSize(new Dimension(100, 30));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                try {
                    ClientSenderThread.getInstance().requestLogin(username, password);
                } catch (Exception ex) {}
            }
        });

        JButton registerButton = new JButton("회원가입");
        registerButton.setPreferredSize(new Dimension(100, 30));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIHandler.getInstance().showSignUpModalUI(LoginUI.this);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 2;

        // 로고 이미지
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(imageLabel, gbc);

        gbc.gridwidth = 1;

        // 아이디 입력
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userText, gbc);

        // 패스워드 입력
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordText, gbc);

        // 로그인 버튼
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(loginButton, gbc);

        // 회원가입 버튼
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(registerButton, gbc);

        setVisible(true);
    }
}