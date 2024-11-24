package com.example.ku_novel.client.ui;

import com.example.ku_novel.domain.NovelRoom;

import javax.swing.*;
import java.awt.*;

public class RoomSearchResultsModalUI extends JDialog {
    private final NovelRoom[] rooms;

    public RoomSearchResultsModalUI(Frame parent, NovelRoom[] rooms) {
        super(parent, "검색 결과", true);
        this.rooms = rooms;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        String[] columnNames = {"제목", "설명", "현재 인원", "최대 인원"};
        String[][] data = new String[rooms.length][4];
        for (int i = 0; i < rooms.length; i++) {
            data[i][0] = rooms[i].getTitle();
            data[i][1] = rooms[i].getDescription();
            // TODO : 서버 쪽에서 현재 방에 있는 인원수를 반환하면 주석해제
            //data[i][2] = String.valueOf(rooms[i].getCurrentParticipants());
            data[i][3] = String.valueOf(rooms[i].getMaxParticipants());
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // 닫기 버튼
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showModal() {
        setVisible(true);
    }
}