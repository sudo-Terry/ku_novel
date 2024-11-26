package com.example.ku_novel.client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class NovelInputModalUI extends JDialog {
    private static final int TIME_LIMIT_SECONDS = 10; // 제한 시간 (초 단위)

    public NovelInputModalUI(JFrame parent) {
        super(parent, "다음 내용 입력", true);
        setLayout(new GridBagLayout());
        setSize(800, 600);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        // 타이머
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel timerLabel = new JLabel("남은 시간 표시");
        timerLabel.setFont(loadCustomFont(16f));

        add(timerLabel, gbc);

        // 이전 소설 내용' 라벨
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel preLabel = new JLabel("이전 소설 내용");
        preLabel.setFont(loadCustomFont(16f));
        add(preLabel, gbc);

        // 이전 소설 내용
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 2.0;
        gbc.fill = GridBagConstraints.BOTH;

        JTextArea preNovelTextArea = new JTextArea(10, 20);
        preNovelTextArea.setFont(loadCustomFont(16f));
        preNovelTextArea.append("이전 소설 내용입니다.");
        preNovelTextArea.setEditable(false);
        add(new JScrollPane(preNovelTextArea), gbc);

        // '내용 입력' 라벨
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputLabel = new JLabel("소설 내용 입력");
        inputLabel.setFont(loadCustomFont(16f));
        add(inputLabel, gbc);

        // 내용 입력
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 2.0;
        gbc.fill = GridBagConstraints.BOTH;

        JTextArea novelInputTextArea = new JTextArea(10, 20);
        novelInputTextArea.setFont(loadCustomFont(16f));
        novelInputTextArea.append("소설 입력창입니다.");
        add(new JScrollPane(novelInputTextArea), gbc);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("등록");
        okButton.setFont(loadCustomFont(16f));
        JButton cancelButton = new JButton("취소");
        cancelButton.setFont(loadCustomFont(16f));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // 확인 버튼 동작
        okButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "등록 완료");
            dispose();
        });

        // 취소 버튼 동작
        cancelButton.addActionListener(e -> {
            dispose();
        });

        // 버튼 패널 추가
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // JDialog 설정
        setLocationRelativeTo(parent);
    }

    private Font loadCustomFont(float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }
}

