package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.ui.component.*;

import javax.swing.*;
import java.awt.*;

public class NameEditModalUI extends JDialog {
    public NameEditModalUI(JFrame parent) {
        super(parent, "닉네임 변경", true);
        setSize(400, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.setBackground(NovelColor.BLACK_GREEN);
        add(mainPanel);

        // 타이틀
        JLabel titleLabel = new JLabel("닉네임 변경");
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(28f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(NovelColor.BLACK_GREEN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);

        Dimension fieldSize = new Dimension(220, 50);

        JTextField nameField = new CustomizedTextField("새 닉네임 입력");
        nameField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        nameField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(nameField, gbc);

        RoundedButton userNameValidationButton = new RoundedButton("중복 확인", NovelColor.LIGHT_GREEN, Color.BLACK);
        userNameValidationButton.setFont(FontSetting.getInstance().loadCustomFont(16f));
        userNameValidationButton.setPreferredSize(new Dimension(80, 50));

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(userNameValidationButton, gbc);

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
