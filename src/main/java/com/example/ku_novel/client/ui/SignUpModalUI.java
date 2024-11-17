package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;

import javax.swing.*;
import java.awt.*;

public class SignUpModalUI extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;

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
        JLabel usernameLabel = new JLabel("아이디:");
        usernameField = new JTextField();
        usernameField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);

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
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        // 유효성 검사
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 회원가입 요청 전송
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}