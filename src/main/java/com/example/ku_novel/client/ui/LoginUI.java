package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.ui.component.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("로그인");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);    // 크기 조절 비활성화
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        Dimension fieldSize = new Dimension(250, 50);
        Dimension buttonSize = new Dimension(250, 50);

        JLabel logoLabel = new JLabel("릴소");
        logoLabel.setFont(FontSetting.getInstance().loadLogoFont(96f));
        logoLabel.setForeground(NovelColor.DARK_GREEN);

        JLabel userLabel = new JLabel("아이디");
        userLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));

        JTextField userText = new CustomizedTextField("아이디");
        userText.setPreferredSize(fieldSize);
        userText.setFont(FontSetting.getInstance().loadCustomFont(16f));


        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));

        JPasswordField passwordText = new CustomizedPasswordField("비밀번호");
        passwordText.setPreferredSize(fieldSize);
        passwordText.setFont(FontSetting.getInstance().loadCustomFont(16f));

        JButton loginButton = new RoundedButton("로그인", NovelColor.DARK_GREEN, Color.WHITE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        loginButton.setPreferredSize(buttonSize);

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
        registerButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setPreferredSize(new Dimension(150, 25));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIHandler.getInstance().showSignUpModalUI(LoginUI.this);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.gridwidth = 2;

        // 로고 이미지
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(logoLabel, gbc);

        // 아이디 입력
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userLabel, gbc);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(userText, gbc);

        // 패스워드 입력
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(passwordText, gbc);

        // 로그인 버튼
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);

        // 회원가입 버튼
        gbc.gridy = 6;
        mainPanel.add(registerButton, gbc);

        setVisible(true);
    }
}