package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;

import javax.swing.*;
import java.awt.*;

public class NovelRoomCreateModalUI extends JDialog {
    private JTextField roomTitleField;
    private JTextField roomDescriptionField;
    private JSpinner novelistTimeSpinner;
    private JSpinner voteTimeSpinner;

    public NovelRoomCreateModalUI(JFrame parent) {
        super(parent, "새로운 소설방 생성", true);
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        Dimension fieldSize = new Dimension(300, 30);

        // 소설방 제목
        JLabel titleLabel = new JLabel("소설방 제목:");
        roomTitleField = new JTextField();
        roomTitleField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(roomTitleField, gbc);

        // 소설방 설명
        JLabel descriptionLabel = new JLabel("소설방 설명:");
        roomDescriptionField = new JTextField();
        roomDescriptionField.setPreferredSize(fieldSize);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(roomDescriptionField, gbc);

        // 소설가 입력 시간
        JLabel novelistTimeLabel = new JLabel("소설가 입력 시간 (분):");
        novelistTimeSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 120, 1)); // 기본값 10, 최소 1, 최대 120, offset 1
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(novelistTimeLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(novelistTimeSpinner, gbc);

        // 소설 투표 시간
        JLabel voteTimeLabel = new JLabel("소설 투표 시간 (분):");
        voteTimeSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1)); // 기본값 5, 최소 1, 최대 60, 증가 단위 1
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(voteTimeLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(voteTimeSpinner, gbc);

        // 확인 버튼
        JButton createButton = new JButton("생성");
        createButton.addActionListener(e -> handleRoomCreation());
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(createButton, gbc);

        // 취소 버튼
        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> dispose());
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(cancelButton, gbc);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    private void handleRoomCreation() {
        String roomTitle = roomTitleField.getText();
        String roomDescription = roomDescriptionField.getText();
        int novelistTime = (int) novelistTimeSpinner.getValue();
        int voteTime = (int) voteTimeSpinner.getValue();

        // 유효성 검사
        if (roomTitle.isEmpty() || roomDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 서버에 소설방 생성 요청
            ClientSenderThread.getInstance().requestCreateNovelRoom(roomTitle, roomDescription, novelistTime, voteTime);
            JOptionPane.showMessageDialog(this, "소설방이 생성되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "소설방 생성 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}