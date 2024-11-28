package com.example.ku_novel.client.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NovelRoomModalUI extends JDialog {
    public NovelRoomModalUI() {
        setTitle("개별 소설방");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);    // 크기 조절 비활성화
        setLocationRelativeTo(null);

        //============= 메인 패널
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        //============= 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topPanel.setPreferredSize(new Dimension(1080, 100));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        topPanel.setBackground(Color.WHITE);

        JPanel novelInfoPanel = new JPanel();
        novelInfoPanel.setLayout(new BoxLayout(novelInfoPanel, BoxLayout.Y_AXIS));
        novelInfoPanel.setPreferredSize(new Dimension(600, 100));
        novelInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        novelInfoPanel.setOpaque(false);

        // 소설 제목
        JLabel titleLabel = new JLabel("소설 제목");
        titleLabel.setFont(loadCustomFont(32f));
        novelInfoPanel.add(titleLabel);

        // 소설 설명
        JLabel descriptionLabel = new JLabel(": 소설 설명");
        descriptionLabel.setFont(loadCustomFont(20f));
        novelInfoPanel.add(descriptionLabel);

        topPanel.add(novelInfoPanel);

        // 소설 참여 인원 수
        JButton participantButton = new JButton("1,000", scaleIcon("src/main/resources/icon/eyes.png", 20, 20));
        participantButton.setFont(loadCustomFont(16f));
        participantButton.setPreferredSize(new Dimension(100, 40));
        participantButton.setBackground(Color.WHITE);
        participantButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        topPanel.add(participantButton);

        // 소설 저장 버튼
        JButton saveButton = new JButton("<html>소설<br>저장</html>");
        saveButton.setPreferredSize(new Dimension(65, 65));
        saveButton.setBackground(Color.WHITE);
        saveButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        topPanel.add(saveButton);

        // 관심 소설 버튼
        JButton interestButton = new JButton("<html>관심<br>소설</html>");
        interestButton.setPreferredSize(new Dimension(65, 65));
        interestButton.setBackground(Color.WHITE);
        interestButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        topPanel.add(interestButton);

        // 소설방 설정 버튼
        JButton settingButton = new JButton("설정");
        settingButton.setPreferredSize(new Dimension(65, 65));
        settingButton.setBackground(Color.WHITE);
        settingButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        topPanel.add(settingButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        //============= 콘텐츠 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));

        // 소설방 버튼 섹션
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(1000, 80));

        // 현재 소설가 버튼
        JButton authorButton = new JButton("현재 소설가 목록");
        authorButton.setPreferredSize(new Dimension(120, 40));
        authorButton.setBackground(Color.WHITE);
        authorButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        buttonPanel.add(authorButton);

        // 소설가 신청 버튼
        JButton applyAuthorButton = new JButton("소설가 신청");
        applyAuthorButton.setPreferredSize(new Dimension(120, 40));
        applyAuthorButton.setBackground(Color.WHITE);
        applyAuthorButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        buttonPanel.add(applyAuthorButton);

        // 소설 작성 버튼
        JButton writeButton = new JButton("(소설가) 소설 작성");
        writeButton.setPreferredSize(new Dimension(120, 40));
        writeButton.setBackground(Color.WHITE);
        writeButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        buttonPanel.add(writeButton);

        writeButton.addActionListener(e-> {
            UIHandler.getInstance().showNovelInputModal(NovelRoomModalUI.this);
        });

        // 투표 버튼
        JButton voteButton = new JButton("(일반) 투표");
        voteButton.setPreferredSize(new Dimension(120, 40));
        voteButton.setBackground(Color.WHITE);
        voteButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        buttonPanel.add(voteButton);

        voteButton.addActionListener(e-> {
            UIHandler.getInstance().showVoteModal(NovelRoomModalUI.this);
        });

        contentPanel.add(buttonPanel);

        //============= 1. "소설" 섹션
        JPanel scrollPanel = new JPanel(new GridLayout(1, 2));

        JPanel novelPanel = new JPanel();
        novelPanel.setLayout(new BoxLayout(novelPanel, BoxLayout.Y_AXIS));

        // 소설 내용
        JTextArea novelTextArea = new JTextArea();
        novelTextArea.setFont(loadCustomFont(20f));
        novelTextArea.setBorder(null);
        novelTextArea.setEditable(false);
        novelTextArea.setLineWrap(true); // 줄바꿈 활성화
        novelTextArea.setWrapStyleWord(true); // 단어 단위로 줄바꿈

        // 스크롤 추가
        JScrollPane novelScrollPane = new JScrollPane(novelTextArea);
        novelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        novelScrollPane.setBorder(null);

        // 자동 스크롤 활성화
        DefaultCaret novelCaret = (DefaultCaret) novelTextArea.getCaret();
        novelCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // 소설 내용
        novelTextArea.append("옛날 옛적 한겨울에, 하늘에서 눈송이가 깃털처럼 내리고 있었습니다.\n");
        novelTextArea.append("그때 어느 왕비가 흑단 나무로 만든 창틀에 앉아 바느질을 하고 있었습니다.\n");
        novelTextArea.append("옛날 옛적 한겨울에, 하늘에서 눈송이가 깃털처럼 내리고 있었습니다.\n");
        novelTextArea.append("그때 어느 왕비가 흑단 나무로 만든 창틀에 앉아 바느질을 하고 있었습니다.\n");
        novelTextArea.append("옛날 옛적 한겨울에, 하늘에서 눈송이가 깃털처럼 내리고 있었습니다.\n");
        novelTextArea.append("그때 어느 왕비가 흑단 나무로 만든 창틀에 앉아 바느질을 하고 있었습니다.\n");
        novelTextArea.append("옛날 옛적 한겨울에, 하늘에서 눈송이가 깃털처럼 내리고 있었습니다.\n");
        novelTextArea.append("그때 어느 왕비가 흑단 나무로 만든 창틀에 앉아 바느질을 하고 있었습니다.\n");
        novelTextArea.append("옛날 옛적 한겨울에, 하늘에서 눈송이가 깃털처럼 내리고 있었습니다.\n");
        novelTextArea.append("그때 어느 왕비가 흑단 나무로 만든 창틀에 앉아 바느질을 하고 있었습니다.\n");
        novelTextArea.append("옛날 옛적 한겨울에, 하늘에서 눈송이가 깃털처럼 내리고 있었습니다.\n");
        novelTextArea.append("그때 어느 왕비가 흑단 나무로 만든 창틀에 앉아 바느질을 하고 있었습니다.\n");
        novelTextArea.append("옛날 옛적 한겨울에, 하늘에서 눈송이가 깃털처럼 내리고 있었습니다.\n");
        novelTextArea.append("그때 어느 왕비가 흑단 나무로 만든 창틀에 앉아 바느질을 하고 있었습니다.\n");

        novelPanel.add(novelScrollPane);

        scrollPanel.add(novelPanel);

        //============= 2. "관심 소설방" 섹션
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

//        // 인기 채팅
//        JTextArea bestChatTextArea = new JTextArea(2, 40);
//        bestChatTextArea.setFont(loadCustomFont(20f));
//        bestChatTextArea.setEditable(false);
//        bestChatTextArea.setLineWrap(true);
//
//        JScrollPane bestChatScrollPane = new JScrollPane(bestChatTextArea);
//        bestChatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        bestChatScrollPane.setBorder(null);
//
//        // 자동 스크롤 활성화
//        DefaultCaret bestChatCaret = (DefaultCaret) bestChatTextArea.getCaret();
//        bestChatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
//
//        // 인기 채팅 내용
//        bestChatTextArea.append("user: 인기 채팅test채팅test채팅test채팅test채팅test채팅test채팅test");

        // 채팅
        JTextArea chatTextArea = new JTextArea(20, 40);
        chatTextArea.setFont(loadCustomFont(20f));
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true); // 줄바꿈 활성화

        // 스크롤을 추가
        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
        novelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.setBorder(null);

        // 자동 스크롤 활성화
        DefaultCaret chatCaret = (DefaultCaret) chatTextArea.getCaret();
        chatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // 테스트 메시지 추가
        for (int i = 0; i < 50; i++) {
            chatTextArea.append("user" + i + ": testtesttesttesttesttesttesttesttesttesttesttesttest \n");
        }

        // 채팅 입력
        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("전송");

        // 하단 패널
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        //chatPanel.add(bestChatScrollPane);
        chatPanel.add(chatScrollPane);
        chatPanel.add(inputPanel);

        scrollPanel.add(chatPanel);
        contentPanel.add(scrollPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private Font loadCustomFont(float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

    private ImageIcon scaleIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}