package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.domain.NovelRoom;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class HomeUI extends JFrame {
    private JPanel mainPanel, topPanel, sidePanel, contentPanel, contentLeftPanel, contentRightPanel;
    private JButton changeButton;
    private JTextField searchField;
    private static final long DEBOUNCE_TIME = 500; // 500ms
    private long lastRequestTime = 0;

    NovelRoom[] testRooms = {
        new NovelRoom(0, "소설방1", "소설 내용이 길어지면 어떻게 되는지 궁금해서 적은 텍스트보다 더 길게 적은 텍스트", "", 0, "ACTIVE", LocalDateTime.now(), "", "hostUser1", null, 5, 3),
        new NovelRoom(0, "소설방2", "소설 내용2", "", 0, "ACTIVE", LocalDateTime.now(), "", "hostUser2", null, 5, 3),
        new NovelRoom(0, "소설방3", "소설 내용2", "", 0, "ACTIVE", LocalDateTime.now(), "", "hostUser3", null, 5, 3),
    };

    public HomeUI() {
        initUI();
    }

    private void initUI() {
        setTitle("릴레이 소설방");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);    // 크기 조절 비활성화
        setLocationRelativeTo(null);

        //============= 메인 패널
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        //============= 상단 패널
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topPanel.setPreferredSize(new Dimension(1080, 100));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        topPanel.setBackground(Color.WHITE);

        //============= 오른쪽 사이드 패널
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(100, 720));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setLayout(new GridLayout(7, 1, 0, 15));

        JButton rankingButton = createIconButton("src/main/resources/icon/ranking.png", 45, 45, Color.LIGHT_GRAY);
        JButton attendanceButton = createIconButton("src/main/resources/icon/calender.png", 45, 45, Color.LIGHT_GRAY);

        changeButton = createIconButton("src/main/resources/icon/my.png", 45, 45, new Color(255, 165, 0));

        sidePanel.add(rankingButton);
        sidePanel.add(attendanceButton);
        for(int i=0; i<4; i++) {
            JPanel jPanel = new JPanel();
            jPanel.setBackground(Color.WHITE);
            sidePanel.add(jPanel);
        }

        sidePanel.add(changeButton);

        //============= 콘텐츠 패널
        contentPanel = new JPanel(new GridLayout(1, 2,10, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        // 콘텐츠 좌/우 패널
        contentLeftPanel = new JPanel();
        contentLeftPanel.setLayout(new BoxLayout(contentLeftPanel, BoxLayout.Y_AXIS));
        contentRightPanel = new JPanel();
        contentRightPanel.setLayout(new BoxLayout(contentRightPanel, BoxLayout.Y_AXIS));

        contentPanel.add(contentLeftPanel);
        contentPanel.add(contentRightPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(sidePanel, BorderLayout.EAST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        showHome();
        setVisible(true);
    }

    private void showHome() {
        resetPanels();

        // 타이틀 변경
        setTitle("릴레이 소설방");

        // 상단 패널 변경
        JLabel titleLabel = new JLabel("릴레이 소설방");
        titleLabel.setPreferredSize(new Dimension(600, 100));
        titleLabel.setFont(loadCustomFont(32f));
        topPanel.add(titleLabel);

        // 검색 필드
        searchField = new JTextField(20);
        searchField.setText("여기에 제목을 입력해 주세요");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("여기에 제목을 입력해 주세요")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText("여기에 제목을 입력해 주세요");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        searchField.setPreferredSize(new Dimension(40, 45));

        // 검색 버튼
        JButton searchButton = createIconButton("src/main/resources/icon/search.png", 45, 45, Color.LIGHT_GRAY);
        searchButton.addActionListener(e -> handleSearchAction());

        // 소설방 만들기 버튼
        JButton createButton = createIconButton("src/main/resources/icon/plus.png", 70, 65, new Color(255, 165, 0));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIHandler.getInstance().showNovelRoomCreateModalUI(HomeUI.this);
            }
        });

        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(createButton);

        // my 버튼
        changeButton.removeActionListener(showHomeListener);
        changeButton.addActionListener(showMyPageListener);
        changeButton.setIcon(scaleIcon("src/main/resources/icon/my.png", 45, 45));

        //============= 1. "참여중인 소설방" 섹션
        JPanel participatingLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel participatingLabel = new JLabel("참여중인 소설방");
        participatingLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        participatingLabelPanel.add(participatingLabel);
        participatingLabel.setFont(loadCustomFont(20f));

        // to-do: 참여 중인 소설방 목록 데이터
        // DefaultTableModel 생성
        DefaultTableModel novelListTM = new DefaultTableModel(new Object[]{"Title", "Description"}, 0);
        for (NovelRoom novel : testRooms) {
            novelListTM.addRow(new Object[]{novel.getTitle(), novel.getDescription()});
        }

        // JTable 생성
        JTable novelListTable = new JTable(novelListTM){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        novelListTable.setFont(loadCustomFont(16f)); // 폰트 설정
        novelListTable.setRowHeight(50);// 각 행의 높이 설정

        TableColumn column1 = novelListTable.getColumnModel().getColumn(0); // 첫 번째 열
        column1.setPreferredWidth(120); // 원하는 너비 설정
        column1.setMinWidth(120);       // 최소 너비 설정
        column1.setMaxWidth(120);       // 최대 너비 설정

        novelListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastRequestTime < DEBOUNCE_TIME) {
                        return;
                    }
                    lastRequestTime = currentTime;

                    int row = novelListTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String roomTitle = (String) novelListTable.getValueAt(row, 0);
                        int roomId = testRooms[row].getId();
                        handleNovelRoomClick(roomId, roomTitle);
                    }
                }
            }
        });

        // 스크롤 추가
        JScrollPane scrollPane = new JScrollPane(novelListTable);
        scrollPane.setBorder(null);

        // 패널에 추가
        JPanel participatingPanel = new JPanel(new BorderLayout());
        participatingPanel.add(scrollPane, BorderLayout.CENTER);

        DefaultListModel<String> novelListModel = new DefaultListModel<>();
        for(int i = 0; i < testRooms.length; i++) {
            novelListModel.addElement("<html>" + testRooms[i].getTitle() + "<br>" + testRooms[i].getDescription() + "</html>");
        }

        contentLeftPanel.add(participatingLabelPanel);
        contentLeftPanel.add(participatingPanel);

        //============= 2. "전체 소설방" 섹션
        JPanel allRoomsLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel allRoomsLabel = new JLabel("전체 소설방");
        allRoomsLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        allRoomsLabelPanel.add(allRoomsLabel);
        allRoomsLabel.setFont(loadCustomFont(20f));

        // to-do: 참여 중인 소설방 목록 데이터
        // DefaultTableModel 생성
        DefaultTableModel allNovelListTM = new DefaultTableModel(new Object[]{"Title", "Description"}, 0);
        for (NovelRoom novel : testRooms) {
            allNovelListTM.addRow(new Object[]{novel.getTitle(), novel.getDescription()});
        }

        // JTable 생성
        JTable allNovelListTable = new JTable(allNovelListTM){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };;
        allNovelListTable.setFont(loadCustomFont(16f)); // 폰트 설정
        allNovelListTable.setRowHeight(50);// 각 행의 높이 설정

        column1 = allNovelListTable.getColumnModel().getColumn(0); // 첫 번째 열
        column1.setPreferredWidth(120); // 원하는 너비 설정
        column1.setMinWidth(120);       // 최소 너비 설정
        column1.setMaxWidth(120);       // 최대 너비 설정

        allNovelListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastRequestTime < DEBOUNCE_TIME) {
                        return;
                    }
                    lastRequestTime = currentTime;

                    int row = novelListTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String roomTitle = (String) novelListTable.getValueAt(row, 0);
                        int roomId = testRooms[row].getId();
                        handleNovelRoomClick(roomId, roomTitle);
                    }
                }
            }
        });

        JScrollPane scrollPane2 = new JScrollPane(allNovelListTable);
        scrollPane2.setBorder(null);

        JPanel allRoomsPanel = new JPanel(new BorderLayout());
        allRoomsPanel.add(scrollPane2, BorderLayout.CENTER);

        contentRightPanel.add(allRoomsLabelPanel);
        contentRightPanel.add(allRoomsPanel);

        refreshPanels();
    }

    private void showMyPage() {
        resetPanels();

        // 타이틀 변경
        setTitle("마이페이지");

        //============= 상단 패널
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

        //============= 홈 버튼
        changeButton.removeActionListener(showMyPageListener);
        changeButton.addActionListener(showHomeListener);
        changeButton.setIcon(scaleIcon("src/main/resources/icon/home.png", 45, 45));

        //============= 콘텐츠 패널
        //============= 1. "내 정보" 섹션
        JPanel infoLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel infoLabel = new JLabel("내 정보");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        infoLabelPanel.add(infoLabel);
        infoLabel.setFont(loadCustomFont(20f));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(500, 800));
        infoPanel.setBackground(Color.WHITE);

        JLabel profileImage = new JLabel(scaleIcon("src/main/resources/sample.jpg", 180, 180));
        profileImage.setPreferredSize(new Dimension(225, 225));

        JPanel infoListPanel = new JPanel(new GridLayout(3, 1));
        infoListPanel.setBackground(Color.WHITE);
        infoListPanel.setPreferredSize(new Dimension(600, 200));
        infoListPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        JLabel idLabel = new JLabel("ID: " + ClientDataModel.getInstance().getUserId());
        idLabel.setFont(loadCustomFont(20f));

        // JLabel nameLabel = new JLabel("Nickname: " + ClientDataModel.getInstance().getUserName());
        JLabel nameLabel = new JLabel("Nickname: 테스트 닉네임");
        nameLabel.setFont(loadCustomFont(20f));

        // JLabel pointLabel = new JLabel("point: " + ClientDataModel.getInstance().getUserPoint());
        JLabel pointLabel = new JLabel("point: 1000");
        pointLabel.setFont(loadCustomFont(20f));

        infoListPanel.add(idLabel);
        infoListPanel.add(nameLabel);
        infoListPanel.add(pointLabel);

        infoPanel.add(profileImage);
        infoPanel.add(infoListPanel);

        contentLeftPanel.add(infoLabelPanel);
        contentLeftPanel.add(infoPanel);

        //============= 2. "관심 소설방" 섹션
        JPanel interestedLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel interestedLabel = new JLabel("관심 소설방");
        interestedLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        interestedLabel.setFont(loadCustomFont(20f));
        interestedLabelPanel.add(interestedLabel);

        // to-do: 관심 소설방 데이터
        // DefaultTableModel 생성
        DefaultTableModel allNovelListTM = new DefaultTableModel(new Object[]{"Title", "Description"}, 0);
        for (NovelRoom novel : testRooms) {
            allNovelListTM.addRow(new Object[]{novel.getTitle(), novel.getDescription()});
        }

        // JTable 생성
        JTable allNovelListTable = new JTable(allNovelListTM);
        allNovelListTable.setFont(loadCustomFont(16f)); // 폰트 설정
        allNovelListTable.setRowHeight(50);// 각 행의 높이 설정

        TableColumn column1 = allNovelListTable.getColumnModel().getColumn(0); // 첫 번째 열
        column1.setPreferredWidth(120); // 원하는 너비 설정
        column1.setMinWidth(120);       // 최소 너비 설정
        column1.setMaxWidth(120);       // 최대 너비 설정

        JScrollPane scrollPane2 = new JScrollPane(allNovelListTable);
        scrollPane2.setBorder(null);

        JPanel allRoomsPanel = new JPanel(new BorderLayout());
        allRoomsPanel.add(scrollPane2, BorderLayout.CENTER);

        contentRightPanel.add(interestedLabelPanel);
        contentRightPanel.add(allRoomsPanel);

        refreshPanels();
    }

    private JButton createIconButton(String iconPath, int width, int height, Color background) {
        JButton button = new JButton(scaleIcon(iconPath, 45, 45));
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(background);
        button.setBorderPainted(false);
        return button;
    }

    private ImageIcon scaleIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
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

    private void resetPanels() {
        topPanel.removeAll();
        contentLeftPanel.removeAll();
        contentRightPanel.removeAll();
    }

    private void refreshPanels() {
        topPanel.revalidate();
        topPanel.repaint();
        contentLeftPanel.revalidate();
        contentLeftPanel.repaint();
        contentRightPanel.revalidate();
        contentRightPanel.repaint();
    }

    ActionListener showHomeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showHome();
        }
    };

    ActionListener showMyPageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showMyPage();
        }
    };

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

    private void handleNovelRoomClick(int roomId, String roomTitle) {
        try {
            ClientSenderThread.getInstance().requestRoomJoin(roomId);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "서버와 연결이 되어 있지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new HomeUI();
    }
}
