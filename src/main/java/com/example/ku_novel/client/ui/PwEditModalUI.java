package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.ui.component.CustomizedPasswordField;
import com.example.ku_novel.client.ui.component.FontSetting;
import com.example.ku_novel.client.ui.component.NovelColor;
import com.example.ku_novel.client.ui.component.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class PwEditModalUI extends JDialog {
    public PwEditModalUI(JFrame parent) {
        super(parent, "비밀번호 변경", true);
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.setBackground(NovelColor.BLACK_GREEN);
        add(mainPanel);

        // 타이틀
        JLabel titleLabel = new JLabel("비밀번호 변경");
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(28f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(NovelColor.BLACK_GREEN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        Dimension fieldSize = new Dimension(250, 50);

        JTextField prePwField = new CustomizedPasswordField("현재 비밀번호 입력");
        prePwField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        prePwField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(prePwField, gbc);

        JTextField pwField = new CustomizedPasswordField("변경할 비밀번호 입력");
        pwField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        pwField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(pwField, gbc);

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
