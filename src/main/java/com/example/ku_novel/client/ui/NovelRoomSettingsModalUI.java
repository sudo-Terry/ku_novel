package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.component.*;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

@Getter
public class NovelRoomSettingsModalUI extends JDialog {
    private JTextField titleField, descriptionField;

    private Boolean isNovelEnded;

    private RoundedButton endNovelButton;

    public NovelRoomSettingsModalUI(JDialog parent) {
        super(parent, "설정", true);
        setLocationRelativeTo(parent);
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
        initData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 20, 10);

        Dimension fieldSize = new Dimension(250, 40);
        Dimension buttonSize = new Dimension(100, 40);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(300, 100));
        topPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        // 상단 제목
        JLabel signLabel = new JLabel("설정");
        signLabel.setFont(FontSetting.getInstance().loadCustomFont(40f));
        signLabel.setForeground(Color.WHITE);
        topPanel.add(signLabel);

        topPanel.setBackground(NovelColor.DARK_GREEN);
        add(topPanel, BorderLayout.NORTH);

        // 소설 제목
        JLabel titleLabel = new JLabel("소설 제목");
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(titleLabel, gbc);

        titleField = new CustomizedTextField("");
        titleField.setPreferredSize(fieldSize);
        titleField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(titleField, gbc);

        // 소설 설명
        JLabel descriptionLabel = new JLabel("소설 설명");
        descriptionLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(descriptionLabel, gbc);

        descriptionField = new CustomizedTextField("");
        descriptionField.setPreferredSize(fieldSize);
        descriptionField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(descriptionField, gbc);

        // 하단 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveButton = new RoundedButton("저장", NovelColor.DARK_GREEN, Color.WHITE);
        saveButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        saveButton.setPreferredSize(buttonSize);
        saveButton.addActionListener(e -> saveSettings());
        buttonPanel.add(saveButton);

        // 소설 종료 버튼
        endNovelButton = new RoundedButton("소설 종료", Color.WHITE, Color.RED, Color.RED);
        endNovelButton.setPreferredSize(buttonSize);
        endNovelButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        endNovelButton.addActionListener(e-> {
            isNovelEnded = !isNovelEnded;
            setEndButton();
        });
        buttonPanel.add(endNovelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
    }

    private void initData() {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        titleField.setText(dataModel.getNovelRoomTitle());
        descriptionField.setText(dataModel.getNovelRoomDescription());
        isNovelEnded = !dataModel.getNovelRoomStatus().equals("ACTIVE");
        setEndButton();
    }

    private void setEndButton() {
        if(isNovelEnded) {
            endNovelButton.setBorderColor(Color.LIGHT_GRAY);
            endNovelButton.setBackground(Color.RED);
            endNovelButton.setForeground(Color.WHITE);
        } else {
            endNovelButton.setBorderColor(Color.RED);
            endNovelButton.setBackground(Color.WHITE);
            endNovelButton.setForeground(Color.RED);
        }
        repaint();
    }

    private void saveSettings() {
        String novelRoomTitle = titleField.getText();
        String novelRoomDescription = descriptionField.getText();
        boolean isNovelEnded = !endNovelButton.isEnabled();

        // 서버로 요청
        ClientSenderThread.getInstance().requestRoomStatusUpdate(
                ClientDataModel.getInstance().getNovelMaxParticipants(), novelRoomTitle, novelRoomDescription, isNovelEnded
        );

        this.dispose();
    }
}