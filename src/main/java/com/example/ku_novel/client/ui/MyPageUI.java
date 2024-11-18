package com.example.ku_novel.client.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyPageUI extends JFrame {
    public MyPageUI() {
        setTitle("마이페이지");
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

        JLabel titleLabel = new JLabel("마이페이지");
        titleLabel.setPreferredSize(new Dimension(600, 100));
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(32f);
            titleLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }
        topPanel.add(titleLabel);

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

        JButton homeButton = new JButton("HOME");
        homeButton.setBackground(new Color(255, 165, 0));
        homeButton.setBorderPainted(false);

        sidePanel.add(rankingButton);
        sidePanel.add(attendanceButton);
        for(int i=0; i<4; i++) {
            JPanel jPanel = new JPanel();
            jPanel.setBackground(Color.WHITE);
            sidePanel.add(jPanel);
        }
        sidePanel.add(homeButton);

        mainPanel.add(sidePanel, BorderLayout.EAST);

        //============= 콘텐츠 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));

        //============= 1. "내 정보" 섹션
        JPanel infoLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel infoLabel = new JLabel("내 정보");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        infoLabelPanel.add(infoLabel);

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            infoLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setPreferredSize(new Dimension(800, 240));
        infoPanel.setBackground(Color.WHITE);

        JLabel profileImage = new JLabel(scaleIcon("src/main/resources/sample.jpg", 180, 180));
        profileImage.setPreferredSize(new Dimension(225, 225));

        JPanel infoListPanel = new JPanel(new GridLayout(3, 1));
        infoListPanel.setBackground(Color.WHITE);
        infoListPanel.setPreferredSize(new Dimension(600, 225));
        infoListPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        JLabel idLabel = new JLabel("ID: userid");
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            idLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }

        JLabel nameLabel = new JLabel("Nickname: username");
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            nameLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }

        JLabel pointLabel = new JLabel("point: 1000");
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            pointLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }

        infoListPanel.add(idLabel);
        infoListPanel.add(nameLabel);
        infoListPanel.add(pointLabel);

        infoPanel.add(profileImage);
        infoPanel.add(infoListPanel);

        contentPanel.add(infoLabelPanel);
        contentPanel.add(infoPanel);

        //============= 2. "관심 소설방" 섹션
        JPanel interestedLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel interestedLabel = new JLabel("관심 소설방");
        interestedLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(20f);
            interestedLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
        }
        interestedLabelPanel.add(interestedLabel);

        // to-do: 관심 소설방 데이터
        String[] interestedRooms = {
                "소설방 1", "소설방 2", "소설방 3", "소설방 4",
                "소설방 5", "소설방 6", "소설방 7", "소설방 8"
        };

        JList<String> allNovelList = new JList<>(interestedRooms);
        allNovelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allNovelList.setVisibleRowCount(1); // 한 줄로 표시
        allNovelList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        allNovelList.setFixedCellWidth(160); // 각 항목의 고정 너비 설정
        allNovelList.setFixedCellHeight(190); // 각 항목의 고정 높이 설정

        JScrollPane scrollPane2 = new JScrollPane(allNovelList);
        scrollPane2.setBorder(null);

        JPanel allRoomsPanel = new JPanel(new BorderLayout());
        allRoomsPanel.add(scrollPane2, BorderLayout.CENTER);

        contentPanel.add(interestedLabelPanel);
        contentPanel.add(allRoomsPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private ImageIcon scaleIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    // 추후 삭제`
    public static void main(String[] args) {
        new MyPageUI();
    }
}