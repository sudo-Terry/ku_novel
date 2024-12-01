package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.ui.component.CustomizedTextField;
import com.example.ku_novel.client.ui.component.FontSetting;
import com.example.ku_novel.client.ui.component.NovelColor;
import com.example.ku_novel.client.ui.component.RoundedButton;
import com.example.ku_novel.domain.NovelRoom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DownloadModalUI extends JDialog {
    private NovelRoom[] rooms;

    private JTextField searchField;

    private JTable table;

    private String[] columnNames = {"제목", "설명"};

    public DownloadModalUI(Frame parent) {
        super(parent, "완결 소설 다운", true);

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel downloadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        downloadPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        downloadPanel.setPreferredSize(new Dimension(400, 80));
        downloadPanel.setBackground(Color.WHITE);

        JLabel downloadLabel = new JLabel("완결 소설");
        downloadLabel.setFont(FontSetting.getInstance().loadCustomFont(40f));
        downloadPanel.add(downloadLabel);
        add(downloadPanel, BorderLayout.NORTH);

        // 비활성화 소설 목록 테이블
        // DefaultTableModel 생성
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        JTable table = new JTable(model);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setFont(FontSetting.getInstance().loadCustomFont(14f));

        table.getTableHeader().setFont(FontSetting.getInstance().loadCustomFont(16f));
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        table.getTableHeader().setBackground(NovelColor.DARK_GREEN);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);

        // 닫기 버튼
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(80, 55));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JButton closeButton = new RoundedButton("닫기", NovelColor.DARK_GREEN, Color.WHITE);
        closeButton.setPreferredSize(new Dimension(80, 35));
        closeButton.setFont(FontSetting.getInstance().loadCustomFont(14f));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showModal() {
        setVisible(true);
    }
}
