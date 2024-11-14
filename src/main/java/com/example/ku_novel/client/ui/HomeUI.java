package com.example.ku_novel.client.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HomeUI extends JFrame {
    public HomeUI() {
        setTitle("릴레이 소설방");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //============= 메인 패널
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        //============= 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topPanel.setPreferredSize(new Dimension(1080, 100));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("릴레이 소설방");
        titleLabel.setPreferredSize(new Dimension(600, 100));
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(32f); // 폰트 크기 16
            titleLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }
        topPanel.add(titleLabel);

        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(40, 45));

        JButton searchButton = new JButton(scaleIcon("src/main/resources/icon/search.png", 45, 45));
        searchButton.setPreferredSize(new Dimension(45, 45));
        searchButton.setBorderPainted(false);
        searchButton.setBackground(Color.LIGHT_GRAY);

        JButton createButton = new JButton(scaleIcon("src/main/resources/icon/plus.png", 45, 45));
        createButton.setPreferredSize(new Dimension(70, 65));
        createButton.setBorderPainted(false);
        createButton.setBackground(new Color(255, 165, 0));

        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(createButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        //============= 오른쪽 사이드 패널
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(100, 720));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setLayout(new GridLayout(7, 1, 0, 15));

        JButton rankingButton = new JButton(scaleIcon("src/main/resources/icon/ranking.png", 45, 45));
        rankingButton.setBackground(Color.WHITE);
        rankingButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));

        JButton attendanceButton = new JButton(scaleIcon("src/main/resources/icon/calender.png", 45, 45));
        attendanceButton.setBackground(Color.WHITE);
        attendanceButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));

        JButton myButton = new JButton(scaleIcon("src/main/resources/icon/my.png", 45, 45));
        myButton.setBackground(new Color(255, 165, 0));
        myButton.setBorderPainted(false);
        sidePanel.add(rankingButton);
        sidePanel.add(attendanceButton);
        for(int i=0; i<4; i++) {
            JPanel jPanel = new JPanel();
            jPanel.setBackground(Color.WHITE);
            sidePanel.add(jPanel);
        }
        sidePanel.add(myButton);

        mainPanel.add(sidePanel, BorderLayout.EAST);

        //============= 콘텐츠 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        //============= 1. "참여중인 소설방" 섹션
        JLabel participatingLabel = new JLabel("참여중인 소설방");
        participatingLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            participatingLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }

        // to-do: 참여 중인 소설방 목록 데이터(
        String[] participatingRooms = {
                "소설방 1", "소설방 2", "소설방 3", "소설방 4",
                "소설방 5", "소설방 6", "소설방 7", "소설방 8"
        };

        JList<String> novelList = new JList<>(participatingRooms);
        novelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        novelList.setVisibleRowCount(1); // to-do: 한 줄만 표시
        novelList.setLayoutOrientation(JList.HORIZONTAL_WRAP); // 가로로 나열
        novelList.setFixedCellWidth(140); // 각 항목의 고정 너비 설정
        novelList.setFixedCellHeight(140); // 각 항목의 고정 높이 설정

        JScrollPane scrollPane = new JScrollPane(novelList);
        scrollPane.setPreferredSize(new Dimension(980, 160));
        scrollPane.setBorder(null);

        JPanel participatingPanel = new JPanel(new BorderLayout());
        participatingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        participatingPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(participatingLabel);
        contentPanel.add(participatingPanel);

        //============= 2. "전체 소설방" 섹션
        JLabel allRoomsLabel = new JLabel("전체 소설방");
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f); // 폰트 크기 16
            allRoomsLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }

        // to-do: 참여 중인 소설방 목록 데이터
        String[] allRooms = {
                "소설방 1", "소설방 2", "소설방 3", "소설방 4",
                "소설방 5", "소설방 6", "소설방 7", "소설방 8"
        };

        JList<String> allNovelList = new JList<>(allRooms);
        allNovelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allNovelList.setVisibleRowCount(2); // to-do: 두 줄 표시(추후 변경)
        allNovelList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        allNovelList.setFixedCellWidth(155); // 각 항목의 고정 너비 설정
        allNovelList.setFixedCellHeight(155); // 각 항목의 고정 높이 설정

        JScrollPane scrollPane2 = new JScrollPane(allNovelList);
        scrollPane2.setPreferredSize(new Dimension(980, 320));
        scrollPane2.setBorder(null);

        JPanel allRoomsPanel = new JPanel(new BorderLayout());
        allRoomsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        allRoomsPanel.add(scrollPane2, BorderLayout.CENTER);

        contentPanel.add(allRoomsLabel);
        contentPanel.add(allRoomsPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private ImageIcon scaleIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public static void main(String[] args) {
        new HomeUI();
    }
}
