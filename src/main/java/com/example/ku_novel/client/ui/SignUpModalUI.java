package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;

import javax.swing.*;
import java.awt.*;

public class SignUpModalUI extends JDialog {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JTextField userNameField;

    public SignUpModalUI(JFrame parent, ClientSenderThread senderThread) {
        super(parent, "회원가입", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        Dimension fieldSize = new Dimension(250, 30);

        // 아이디 입력
        JLabel userIdLabel = new JLabel("아이디:");
        userIdField = new JTextField();
        userIdField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userIdField, gbc);

        JButton idValidationButton = new JButton("아이디 중복 확인");
        idValidationButton.addActionListener(e -> handleUserIdValidation(senderThread));
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(idValidationButton, gbc);

        // 비밀번호 입력
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // 닉네임 입력
        JLabel userNameLabel = new JLabel("닉네임:");
        userNameField = new JTextField();
        userNameField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(userNameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userNameField, gbc);

        JButton userNameValidationButton = new JButton("닉네임 중복 확인");
        userNameValidationButton.addActionListener(e -> handleUserNameValidation(senderThread));
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userNameValidationButton, gbc);

        // 회원가입 버튼
        JButton signUpButton = new JButton("회원가입");
        signUpButton.addActionListener(e -> handleSignUp(senderThread));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(signUpButton, gbc);

        // 취소 버튼
        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> dispose());
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(cancelButton, gbc);

        setVisible(true);
    }

    private void handleSignUp(ClientSenderThread senderThread) {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());
        String userName = userNameField.getText();

        // 유효성 검사
        if (userId.isEmpty() || password.isEmpty() || userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            senderThread.requestSignUp(userId, password, userName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleUserIdValidation(ClientSenderThread senderThread) {
        String userId = userIdField.getText();

        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            senderThread.requestIdValidation(userId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleUserNameValidation(ClientSenderThread senderThread) {
        String userName = userNameField.getText();

        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "닉네임 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            senderThread.requestNicknameValidation(userName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}