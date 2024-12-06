package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.component.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MyPageModalUI extends JDialog {
    public MyPageModalUI(JFrame parent) {
        super(parent, "마이페이지", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        init(parent);
    }

    private void init(JFrame frame) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        JPanel topPanel = new JPanel();

        JLabel titleLabel = new JLabel("마이페이지");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 110, 0, 0));
        titleLabel.setPreferredSize(new Dimension(600, 100));
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(36f));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        //============= 콘텐츠 패널
        //============= 1. "내 정보" 섹션
        mainPanel.setBackground(NovelColor.BLACK_GREEN);

        JPanel infoLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoLabelPanel.setBackground(NovelColor.BLACK_GREEN);
        JLabel infoLabel = new JLabel("내 정보");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(FontSetting.getInstance().loadCustomFont(28f));
        infoLabelPanel.add(infoLabel);

        mainPanel.add(infoLabelPanel);

        JPanel infoContentPanel = new JPanel(new GridBagLayout());
        infoContentPanel.setBackground(NovelColor.BLACK_GREEN);
        GridBagConstraints mainGbc = new GridBagConstraints();

        JPanel profileImage = new CircularImage("src/main/resources/image/image1.png");
        profileImage.setPreferredSize(new Dimension(245, 245));
        mainGbc.insets = new Insets(0, 0, 10, 0);
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.fill = GridBagConstraints.BOTH;
        infoContentPanel.add(profileImage, mainGbc);

        // JLabel nameLabel = new JLabel("Nickname: " + ClientDataModel.getInstance().getUserName());
        JPanel namePanel = new JPanel();
        JLabel nameLabel;
        if(ClientDataModel.getInstance().getUserName() == null) {
            nameLabel = new JLabel("NICKNAME");
        } else {
            nameLabel = new JLabel(ClientDataModel.getInstance().getUserName());
        }
        nameLabel.setFont(FontSetting.getInstance().loadCustomFont(28f));
        nameLabel.setForeground(Color.WHITE);
        namePanel.add(nameLabel);
        namePanel.setOpaque(false);

        mainGbc.gridy = 2;
        mainGbc.insets = new Insets(0, 0, 0, 0);
        infoContentPanel.add(namePanel, mainGbc);

        JPanel idPanel = new JPanel();
        JLabel idLabel;
        if(ClientDataModel.getInstance().getUserId() == null) {
            idLabel = new JLabel("userID");
        } else {
            idLabel = new JLabel(ClientDataModel.getInstance().getUserId());
        }
        idLabel.setFont(FontSetting.getInstance().loadCustomFont(20f));
        idLabel.setForeground(NovelColor.DEFAULT);
        idPanel.add(idLabel);
        idPanel.setOpaque(false);

        mainGbc.gridy = 3;
        infoContentPanel.add(idPanel, mainGbc);

        mainPanel.add(infoContentPanel);

        // 포인트 패널
        JPanel pointPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pointPanel.setMaximumSize(new Dimension(200, 100));
        pointPanel.setBackground(NovelColor.BLACK_GREEN);
        pointPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        // JLabel pointLabel = new JLabel("point: " + ClientDataModel.getInstance().getUserPoint());
        JLabel pointLabel;
        if(ClientDataModel.getInstance().getUserPoint() == null) {
            pointLabel = new JLabel("1000");
        } else {
            pointLabel = new JLabel(ClientDataModel.getInstance().getUserPoint());
        }
        pointLabel.setFont(FontSetting.getInstance().loadCustomFont(20f));
        pointLabel.setForeground(Color.WHITE);
        pointPanel.add(pointLabel);

        JLabel pointTitleLabel = new JLabel("POINT");
        pointTitleLabel.setFont(FontSetting.getInstance().loadCustomFont(20f));
        pointTitleLabel.setForeground(Color.WHITE);
        pointPanel.add(pointTitleLabel);

        mainPanel.add(pointPanel);

        // 버튼 패널 - 추후 프로필 변경 기능 추가시 사용
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setPreferredSize(new Dimension(300, 70));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.setBackground(NovelColor.BLACK_GREEN);

        Dimension buttonSize = new Dimension(120, 40);

        JButton imageEditButton = new RoundedButton("프로필 변경", NovelColor.BLACK_GREEN, Color.WHITE);
        imageEditButton.setPreferredSize(buttonSize);
        imageEditButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        imageEditButton.addActionListener(e->UIHandler.getInstance().showImageEditModal(frame));
        buttonPanel.add(imageEditButton);

        JButton pwEditButton = new RoundedButton("비밀번호 변경", NovelColor.BLACK_GREEN, Color.WHITE);
        pwEditButton.addActionListener(e->UIHandler.getInstance().showPwEditModal(frame));
        pwEditButton.setPreferredSize(buttonSize);
        pwEditButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        buttonPanel.add(pwEditButton);

        JButton nameEditButton = new RoundedButton("닉네임 변경", NovelColor.BLACK_GREEN, Color.WHITE);
        nameEditButton.setPreferredSize(buttonSize);
        nameEditButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        nameEditButton.addActionListener(e->UIHandler.getInstance().showNameEditModal(frame));
        buttonPanel.add(nameEditButton);

        mainPanel.add(buttonPanel);
    }

    public void showModal() {
        setVisible(true);
    }
}
