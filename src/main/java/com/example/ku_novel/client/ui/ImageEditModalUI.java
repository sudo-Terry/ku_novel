package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.ui.component.FontSetting;
import com.example.ku_novel.client.ui.component.NovelColor;
import com.example.ku_novel.client.ui.component.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class ImageEditModalUI extends JDialog {

    public ImageEditModalUI(JFrame parent) {
        super(parent, "프로필 사진 변경", true);
        setSize(600, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(NovelColor.BLACK_GREEN);
        add(mainPanel);

        // 타이틀
        JLabel titleLabel = new JLabel("프로필 이미지 변경");
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(28f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 프로필 이미지
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 70, 10, 70));
        contentPanel.setBackground(NovelColor.BLACK_GREEN);

        JButton[] imageButtons = new JButton[4];
        int[] selectedIndex = {-1}; // 선택된 버튼 인덱스를 저장

        for (int i = 0; i < 4; i++) {
            ImageIcon imageIcon = new ImageIcon("src/main/resources/image/image" + (i + 1) + ".png");
            Image scaledImage = imageIcon.getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JButton imageButton = new JButton(scaledIcon);
            imageButton.setPreferredSize(new Dimension(150, 150));
            imageButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            imageButton.setFocusPainted(false);
            imageButton.setBackground(Color.WHITE);

            int buttonIndex = i;
            imageButton.addActionListener(e -> {
                if (selectedIndex[0] != -1) {
                    imageButtons[selectedIndex[0]].setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                }
                imageButton.setBorder(BorderFactory.createLineBorder(NovelColor.YELLOW, 5));
                selectedIndex[0] = buttonIndex;
            });

            imageButtons[i] = imageButton;
            contentPanel.add(imageButton);
        }
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // 확인 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(NovelColor.BLACK_GREEN);

        JButton okButton = new RoundedButton("변경", NovelColor.YELLOW, Color.BLACK);
        okButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        okButton.setPreferredSize(new Dimension(80, 40));
        buttonPanel.add(okButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    }

    public void showModal() {
        setVisible(true);
    }
}
