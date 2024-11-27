package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.ui.component.*;

import javax.swing.*;
import java.awt.*;

public class SignUpModalUI extends JDialog {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JTextField userNameField;

    public SignUpModalUI(JFrame parent) {
        super(parent, "회원가입", true);
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 20, 10);

        Dimension fieldSize = new Dimension(240, 45);
        Dimension buttonSize = new Dimension(120, 45);

        // 회원 가입 제목
        JLabel signLabel = new JLabel("회원가입");
        signLabel.setFont(FontSetting.getInstance().loadCustomFont(48f));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(signLabel, gbc);

        // 아이디 입력;
        gbc.insets = new Insets(10, 10, 0, 0);

        JLabel userIdLabel = new JLabel("아이디");
        userIdLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userIdLabel, gbc);

        userIdField = new CustomizedTextField("아이디");
        userIdField.setPreferredSize(fieldSize);
        userIdField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(userIdField, gbc);

        RoundedButton idValidationButton = new RoundedButton("중복 확인", Color.WHITE, NovelColor.DARK_GREEN);
        idValidationButton.setBorderColor(NovelColor.DARK_GREEN);
        idValidationButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        idValidationButton.setPreferredSize(buttonSize);
        idValidationButton.addActionListener(e -> handleUserIdValidation());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(idValidationButton, gbc);

        // 비밀번호 입력
        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new CustomizedPasswordField("비밀번호");
        passwordField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        passwordField.setPreferredSize(new Dimension(370, 45));
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(passwordField, gbc);

        // 닉네임 입력
        JLabel userNameLabel = new JLabel("닉네임");
        userNameLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userNameLabel, gbc);

        userNameField = new CustomizedTextField("닉네임");
        userNameField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        userNameField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(userNameField, gbc);

        RoundedButton userNameValidationButton = new RoundedButton("중복 확인", Color.WHITE, NovelColor.DARK_GREEN);
        userNameValidationButton.setBorderColor(NovelColor.DARK_GREEN);
        userNameValidationButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        userNameValidationButton.setPreferredSize(buttonSize);
        userNameValidationButton.addActionListener(e -> handleUserNameValidation());
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(userNameValidationButton, gbc);

        // 회원가입 버튼
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(370,50));

        JButton signUpButton = new RoundedButton("회원가입", NovelColor.DARK_GREEN, Color.WHITE);
        signUpButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        signUpButton.setPreferredSize(new Dimension(150,50));
        signUpButton.addActionListener(e -> handleSignUp());

        // 취소 버튼
        RoundedButton cancelButton = new RoundedButton("취소", Color.WHITE, Color.GRAY);
        cancelButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        cancelButton.setPreferredSize(new Dimension(150,50));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(signUpButton);
        buttonPanel.add(cancelButton);

        gbc.insets = new Insets(45, 10, 30, 10);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    private void handleSignUp() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());
        String userName = userNameField.getText();

        // 유효성 검사
        if (userId.isEmpty() || password.isEmpty() || userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ClientSenderThread.getInstance().requestSignUp(userId, password, userName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleUserIdValidation() {
        String userId = userIdField.getText();

        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ClientSenderThread.getInstance().requestIdValidation(userId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleUserNameValidation() {
        String userName = userNameField.getText();

        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "닉네임 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ClientSenderThread.getInstance().requestNicknameValidation(userName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}