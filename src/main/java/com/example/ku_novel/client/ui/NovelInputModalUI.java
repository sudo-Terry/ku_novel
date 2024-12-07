package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.component.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

public class NovelInputModalUI extends JDialog {
    private JTextArea novelInputTextArea;

    public NovelInputModalUI(NovelRoomModalUI parent) {
        super(parent, "다음 내용 입력", true);
        setBackground(Color.WHITE);
        setSize(720, 540);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 10));
        mainPanel.setBackground(Color.WHITE);

        add(mainPanel);

        JLabel titleLabel = new JLabel("소설 작성");
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(28f));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.WHITE);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        novelInputTextArea = new JTextArea(10, 20);
        novelInputTextArea.setFont(FontSetting.getInstance().loadCustomFont(20f));

        String placeholderText = "다음 내용 소설 입력창입니다.";
        novelInputTextArea.setText(placeholderText);
        novelInputTextArea.setForeground(Color.GRAY);

        novelInputTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (novelInputTextArea.getText().equals(placeholderText)) {
                    novelInputTextArea.setText("");
                    novelInputTextArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (novelInputTextArea.getText().isEmpty()) {
                    novelInputTextArea.setText(placeholderText);
                    novelInputTextArea.setForeground(Color.GRAY);
                }
            }
        });

        // 스크롤 추가
        JScrollPane inputScrollPane = new JScrollPane(novelInputTextArea);
        inputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        inputScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        mainPanel.add(inputScrollPane, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        CountdownButtonUtility countdownButtonUtility = new CountdownButtonUtility(
                ClientDataModel.getInstance().getCountDownWrite(),
                "등록"
        );
        JButton okButton = countdownButtonUtility.getButton();
        countdownButtonUtility.startCountdown();
        okButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        okButton.setPreferredSize(new Dimension(100, 40));

        JButton cancelButton = new RoundedButton("취소", Color.WHITE, Color.DARK_GRAY);
        cancelButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        cancelButton.setPreferredSize(new Dimension(100, 40));

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // 확인 버튼 동작
        okButton.addActionListener(e -> {
            handleSubmitClicked();
        });

        // 취소 버튼 동작
        cancelButton.addActionListener(e -> {
            dispose();
        });
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // JDialog 설정
        setLocationRelativeTo(parent);
    }

    private void handleSubmitClicked() {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        String userId = dataModel.getUserId();
        int novelRoomId = dataModel.getCurrentRoomId();
        String content = novelInputTextArea.getText();

        if (content.isEmpty()) {
            CustomAlert.showAlert(this, "오류", "모든 필드를 입력하세요.", null);
            //JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ClientSenderThread.getInstance().requestAuthorWrite(userId, novelRoomId, content);
            dispose();
        } catch (Exception ex) {
            CustomAlert.showAlert(this, "오류", "제출 중 오류가 발생하였습니다.", null);
            //JOptionPane.showMessageDialog(this, "제출 중 오류가 발생하였습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}