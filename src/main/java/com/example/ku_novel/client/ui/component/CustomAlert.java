package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CustomAlert extends JDialog {
    private static final int BASE_FONT_SIZE = 18;
    private static final int MIN_WIDTH = 300;
    private static final int MAX_WIDTH = 600;
    private static final int MIN_HEIGHT = 150;

    public CustomAlert(Window owner, String title, String message, ActionListener onConfirm) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setResizable(false);

        // 글자 크기와 창 크기 계산
        int messageLength = message.length();
        int fontSize = calculateFontSize(messageLength);
        int dialogWidth = calculateDialogWidth(messageLength);

        setSize(dialogWidth, MIN_HEIGHT);
        setLocationRelativeTo(owner);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // 메시지 라벨
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(FontSetting.getInstance().loadCustomFont((float) fontSize));
        mainPanel.add(messageLabel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        // 확인 버튼
        JButton confirmButton = new RoundedButton("확인", NovelColor.DARK_GREEN, Color.WHITE);
        confirmButton.setFont(FontSetting.getInstance().loadCustomFont(14f));
        confirmButton.setPreferredSize(new Dimension(80, 30));
        confirmButton.addActionListener(e -> {
            if (onConfirm != null) onConfirm.actionPerformed(e);
            dispose();
        });

        buttonPanel.add(confirmButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // 정적 메서드로 간편 호출
    public static void showAlert(Window owner, String title, String message, ActionListener onConfirm) {
        CustomAlert alert = new CustomAlert(owner, title, message, onConfirm);
        alert.setVisible(true);
    }

    // 글자 수에 따라 폰트 크기 계산
    private int calculateFontSize(int messageLength) {
        if (messageLength <= 50) {
            return BASE_FONT_SIZE;
        } else if (messageLength <= 100) {
            return BASE_FONT_SIZE - 2; // 글자 수가 많을수록 폰트 크기 감소
        } else {
            return BASE_FONT_SIZE - 4;
        }
    }

    // 글자 수에 따라 창 너비 계산
    private int calculateDialogWidth(int messageLength) {
        return Math.min(MAX_WIDTH, MIN_WIDTH + messageLength * 8); // 글자 수에 따라 너비 증가
    }
}
