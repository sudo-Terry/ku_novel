package com.example.ku_novel.client.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NovelRoomUI extends JFrame {
    public NovelRoomUI() {
        setTitle("개별 소설방");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // 소설 제목
        JLabel titleLabel = new JLabel("소설 제목");
        titleLabel.setPreferredSize(new Dimension(800, 100));
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(32f);
            titleLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }
        topPanel.add(titleLabel);

        // 관심 소설 버튼
        JButton interestButton = new JButton("<html>관심<br>소설</html>");
        interestButton.setPreferredSize(new Dimension(65, 65));
        interestButton.setBackground(Color.WHITE);
        interestButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        topPanel.add(interestButton);

        // 소설방 나가기 버튼
        JButton exitButton = new JButton("<html>소설방<br>나가기</html>");
        exitButton.setPreferredSize(new Dimension(65, 65));
        exitButton.setBackground(Color.WHITE);
        exitButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        topPanel.add(exitButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        //============= 콘텐츠 패널
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        //============= 1. "소설" 섹션
        JPanel novelPanel = new JPanel();
        novelPanel.setLayout(new BoxLayout(novelPanel, BoxLayout.Y_AXIS));

        JPanel infoLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoLabelPanel.setMaximumSize(new Dimension(550, 50));
        infoLabelPanel.setBackground(Color.WHITE);

        // 현재 소설가 버튼
        JButton authorButton = new JButton("현재 소설가 목록");
        authorButton.setPreferredSize(new Dimension(120, 40));
        authorButton.setBackground(Color.WHITE);
        authorButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        infoLabelPanel.add(authorButton);

        // 소설 참여 인원 수
        JLabel participantLabel = new JLabel("1,000");
        participantLabel.setPreferredSize(new Dimension(360, 40));
        participantLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(24f);
            participantLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }
        infoLabelPanel.add(participantLabel);


        // 소설 내용
        JTextArea novelTextArea = new JTextArea();
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            novelTextArea.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }
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

        novelPanel.add(infoLabelPanel);
        novelPanel.add(novelScrollPane);

        contentPanel.add(novelPanel);

        //============= 2. "관심 소설방" 섹션
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        // 인기 채팅
        JTextArea bestChatTextArea = new JTextArea(2, 40);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            bestChatTextArea.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }
        bestChatTextArea.setEditable(false);
        bestChatTextArea.setLineWrap(true);

        JScrollPane bestChatScrollPane = new JScrollPane(bestChatTextArea);
        bestChatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bestChatScrollPane.setBorder(null);

        // 자동 스크롤 활성화
        DefaultCaret bestChatCaret = (DefaultCaret) bestChatTextArea.getCaret();
        bestChatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // 인기 채팅 내용
        bestChatTextArea.append("user: 인기 채팅test채팅test채팅test채팅test채팅test채팅test채팅test");

        // 채팅
        JTextArea chatTextArea = new JTextArea(20, 40);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            chatTextArea.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }
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
        JButton sendButton = new JButton("Send");

        // 하단 패널
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(bestChatScrollPane);
        chatPanel.add(chatScrollPane);
        chatPanel.add(inputPanel);

        contentPanel.add(chatPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setVisible(true);

        System.out.println("height: "+infoLabelPanel.getHeight());
    }

    // 추후 삭제`
    public static void main(String[] args) {
        new NovelRoomUI();
    }
}