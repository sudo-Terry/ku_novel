package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;

import javax.swing.*;
import java.awt.*;

public class AuthorAcceptModalUI extends JDialog {

    private String requestUserNickname;

    public AuthorAcceptModalUI(JFrame parent, String requestUserNickname) {
        super(parent, "소설가 승인 요청", true);
        this.requestUserNickname = requestUserNickname;

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(400, 200);
        setLocationRelativeTo(null);

        JLabel messageLabel = new JLabel(
                String.format("<html><center>%s 님이 소설가가 되는 것을 요청했습니다.<br>승인하시겠습니까?</center></html>", requestUserNickname),
                SwingConstants.CENTER
        );
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton approveButton = new JButton("승인");
        approveButton.setPreferredSize(new Dimension(100, 40));
        approveButton.addActionListener(e -> {
            approveUser();
            dispose();
        });

        JButton rejectButton = new JButton("거부");
        rejectButton.setPreferredSize(new Dimension(100, 40));
        rejectButton.addActionListener(e -> {
            rejectUser();
            dispose();
        });

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void approveUser() {
        System.out.println(requestUserNickname + " 님이 소설가로 승인되었습니다.");
        // ClientSenderThread.getInstance().requestAuthorApproved();
    }

    private void rejectUser() {
        System.out.println(requestUserNickname + " 님의 소설가 요청이 거부되었습니다.");
        // ClientSenderThread.getInstance().requestAuthorRejected();
    }
}