package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
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
import java.util.Arrays;

public class RoomSearchResultsModalUI extends JDialog {
    private NovelRoom[] rooms;

    private JTextField searchField;

    private JTable table;

    private String[] columnNames = {"제목", "설명", "현재 소설가 수", "최대 소설가 수"};

    public RoomSearchResultsModalUI(Frame parent) {
        super(parent, "검색", true);

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        searchPanel.setPreferredSize(new Dimension(400, 65));
        searchPanel.setBackground(Color.WHITE);

        // 검색 필드
        searchField = new CustomizedTextField("제목으로 검색");
        searchField.setPreferredSize(new Dimension(300, 45));
        searchField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        searchPanel.add(searchField);

        // 검색 버튼
        JButton searchButton = createIconButton("src/main/resources/icon/search.png", NovelColor.DARK_GREEN);
        searchButton.addActionListener(e -> handleSearchAction());
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // 검색 결과 테이블
        // DefaultTableModel 생성
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        this.table = new JTable(model);
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

    private JButton createIconButton(String iconPath, Color background) {
        JButton button = new JButton(scaleIcon(iconPath, 35, 35));
        button.setPreferredSize(new Dimension(45, 45));
        button.setBackground(background);
        button.setBorderPainted(false);
        return button;
    }

    private ImageIcon scaleIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void handleSearchAction() {
        String roomTitle = searchField.getText().trim();
        if (roomTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "방 제목을 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ClientSenderThread.getInstance().requestRoomFetchByTitle(roomTitle);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "서버와 연결이 되어 있지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showModal() {
        setVisible(true);
    }

    public void showRoomResult() {
        this.rooms = ClientDataModel.getInstance().getChatRoomsSearchResult().toArray(new NovelRoom[0]);

        System.out.println(Arrays.toString(rooms));

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (NovelRoom room : rooms) {
            model.addRow(new Object[]{
                    room.getTitle(),
                    room.getDescription(),
                    room.getCurrentParticipantCount(),
                    room.getMaxParticipants()
            });
        }
    }
}