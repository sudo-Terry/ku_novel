package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.component.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NovelInputModalUI extends JDialog {
    private JTextArea novelInputTextArea;
    private static final int TIME_LIMIT_SECONDS = 10; // 제한 시간 (초 단위)

    public NovelInputModalUI(NovelRoomModalUI parent) {
        super(parent, "다음 내용 입력", true);
        setBackground(Color.WHITE);
        setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 10));
        mainPanel.setBackground(Color.WHITE);

        add(mainPanel);

        JLabel titleLabel = new JLabel("소설 작성");
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(28f));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.WHITE);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 0, 20);

        // 이전 소설 내용' 라벨
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel preLabel = new JLabel("이전 소설 내용");
        preLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));
        contentPanel.add(preLabel, gbc);

        // 이전 소설 내용
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 2.0;
        gbc.fill = GridBagConstraints.BOTH;

        JTextArea preNovelTextArea = new JTextArea(10, 20);
        preNovelTextArea.setFont(FontSetting.getInstance().loadCustomFont(16f));
        preNovelTextArea.append("이전 소설 내용입니다.");
        preNovelTextArea.setEditable(false);

        // 스크롤 추가
        JScrollPane preScrollPane = new JScrollPane(preNovelTextArea);
        preScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        preScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        preScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        contentPanel.add(preScrollPane, gbc);

        // '내용 입력' 라벨
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputLabel = new JLabel("소설 내용 입력");
        inputLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));
        contentPanel.add(inputLabel, gbc);

        // 내용 입력
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 2.0;
        gbc.fill = GridBagConstraints.BOTH;

        novelInputTextArea = new JTextArea(10, 20);
        novelInputTextArea.setFont(FontSetting.getInstance().loadCustomFont(16f));
        novelInputTextArea.append("소설 입력창입니다.");

        // 스크롤 추가
        JScrollPane inputScrollPane = new JScrollPane(novelInputTextArea);
        inputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        inputScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        contentPanel.add(inputScrollPane, gbc);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new RoundedButton("등록(10:00)", NovelColor.DARK_GREEN, Color.WHITE);
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