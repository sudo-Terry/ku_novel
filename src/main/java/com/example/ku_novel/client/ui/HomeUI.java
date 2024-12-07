package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.component.*;
import com.example.ku_novel.domain.NovelRoom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HomeUI extends JFrame {
    private static HomeUI instance;
    private JPanel mainPanel, topPanel, leftButtonPanel, rightButtonPanel, contentPanel, contentLeftPanel, contentRightPanel;
    private JButton myButton, searchButton, rankingButton, attendanceButton, downloadButton, createButton;
    private JLabel changeLabel;
    private static final long DEBOUNCE_TIME = 500; // 500ms
    private long lastRequestTime = 0;
    private List<NovelRoom> participatingNovelRooms;
    private List<NovelRoom> activeNovelRooms;

    public HomeUI() {
        initDatas();
        initUI();
    }

    public static HomeUI getInstance() {
        if (instance == null) {
            synchronized (HomeUI.class) {
                if (instance == null) {
                    instance = new HomeUI();
                }
            }
        }
        return instance;
    }

    private void initUI() {
        setTitle("릴소");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);    // 크기 조절 비활성화
        setLocationRelativeTo(null);

        //============= 메인 패널
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        //============= 상단 패널
        topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1080, 100));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        topPanel.setBackground(Color.WHITE);

        //============= 콘텐츠 패널
        contentPanel = new JPanel(new GridLayout(1, 2,5, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(Color.WHITE);

        // 콘텐츠 좌/우 패널
        contentLeftPanel = new JPanel();
        contentLeftPanel.setLayout(new BoxLayout(contentLeftPanel, BoxLayout.Y_AXIS));
        contentLeftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentRightPanel = new JPanel();
        contentRightPanel.setLayout(new BoxLayout(contentRightPanel, BoxLayout.Y_AXIS));
        contentRightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(contentLeftPanel);
        contentPanel.add(contentRightPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        showHome();
        setVisible(true);
    }

    private void initLeftButtonPanel() {
        leftButtonPanel = new JPanel(new GridBagLayout());
        leftButtonPanel.setPreferredSize(new Dimension(250, 90));
        leftButtonPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 0, 0);

        // 파일 다운 버튼
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        downloadButton = new ImageButton("src/main/resources/icon/file.png", Color.LIGHT_GRAY);
        downloadButton.addActionListener(e -> {
            ClientSenderThread.getInstance().requestRoomFetchByCompleted();
        });
        leftButtonPanel.add(downloadButton, gbc);

        JLabel downloadLabel = new JLabel("완결 소설");
        downloadLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridy = 1;
        leftButtonPanel.add(downloadLabel, gbc);

        // 랭킹 버튼
        gbc.gridx = 1;
        gbc.gridy = 0;
        rankingButton = new ImageButton("src/main/resources/icon/ranking.png", Color.LIGHT_GRAY);
        rankingButton.addActionListener(e -> {
            ClientSenderThread.getInstance().requestRoomFetchRank();
        });
        leftButtonPanel.add(rankingButton, gbc);

        JLabel rankingLabel = new JLabel("랭킹");
        rankingLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridy = 1;
        leftButtonPanel.add(rankingLabel, gbc);

        // 출석 체크 버튼
        gbc.gridx = 2;
        gbc.gridy = 0;
        attendanceButton = new ImageButton("src/main/resources/icon/calender.png", Color.LIGHT_GRAY);
        attendanceButton.addActionListener(e-> {
            ClientSenderThread.getInstance().requestAttendanceCheck(
                ClientDataModel.getInstance().getUserId()
            );
        });
        leftButtonPanel.add(attendanceButton, gbc);

        JLabel attendanceLabel = new JLabel("출석");
        attendanceLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridy = 1;
        leftButtonPanel.add(attendanceLabel, gbc);
    }

    private void initRightButtonPanel() {
        rightButtonPanel = new JPanel(new GridBagLayout());
        rightButtonPanel.setPreferredSize(new Dimension(250, 90));
        rightButtonPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 0, 15);

        // 검색 버튼
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        searchButton = new ImageButton("src/main/resources/icon/search.png", NovelColor.DARK_GREEN);
        searchButton.addActionListener(e -> UIHandler.getInstance().showRoomSearchModal(HomeUI.this));
        rightButtonPanel.add(searchButton, gbc);

        JLabel searchLabel = new JLabel("검색");
        searchLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridy = 1;
        rightButtonPanel.add(searchLabel, gbc);

        // 소설방 만들기 버튼
        gbc.gridx = 1;
        gbc.gridy = 0;
        createButton = new ImageButton("src/main/resources/icon/plus.png", NovelColor.DARK_GREEN);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIHandler.getInstance().showNovelRoomCreateModalUI(HomeUI.this);
            }
        });
        rightButtonPanel.add(createButton, gbc);

        JLabel createLabel = new JLabel("방 만들기");
        createLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridy = 1;
        rightButtonPanel.add(createLabel, gbc);

        // my page 버튼
        gbc.gridx = 2;
        gbc.gridy = 0;
        myButton = new ImageButton("src/main/resources/icon/my.png", NovelColor.DARK_GREEN);
        myButton.addActionListener(e->UIHandler.getInstance().showMyPageModal(this));
        rightButtonPanel.add(myButton, gbc);

        changeLabel = new JLabel("마이페이지");
        changeLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        gbc.gridy = 1;
        rightButtonPanel.add(changeLabel, gbc);


        // 새로고침 버튼
//        gbc.gridx = 3;
//        gbc.gridy = 0;
//        createButton = new ImageButton("src/main/resources/icon/refresh.png", NovelColor.DARK_GREEN);
//        createButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String userId = ClientDataModel.getInstance().getUserId();
//                ClientSenderThread.getInstance().requestRefreshHome(userId);
//            }
//        });
//        rightButtonPanel.add(createButton, gbc);
//
//        JLabel refreshLabel = new JLabel("새로고침");
//        refreshLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
//        gbc.gridy = 1;
//        rightButtonPanel.add(refreshLabel, gbc);
    }

    private void showHome() {
        resetPanels();
        contentLeftPanel.setBackground(NovelColor.DEFAULT);

        // 상단 패널 변경
        JLabel titleLabel = new JLabel("릴소");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 90, 0, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(FontSetting.getInstance().loadLogoFont(54f));
        titleLabel.setForeground(NovelColor.DARK_GREEN);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        //============= 버튼 패널
        initLeftButtonPanel();
        initRightButtonPanel();
        topPanel.add(leftButtonPanel, BorderLayout.WEST);
        topPanel.add(rightButtonPanel, BorderLayout.EAST);

        //============= 1. "참여중인 소설방" 섹션
        JPanel participatingLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel participatingLabel = new JLabel("참여중인 소설방");
        participatingLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        participatingLabelPanel.add(participatingLabel);
        participatingLabel.setFont(FontSetting.getInstance().loadCustomFont(20f));

        // 참여 중인 소설방 목록 데이터
        // DefaultTableModel 생성
        DefaultTableModel novelListTM = new DefaultTableModel(new Object[]{"제목", "설명"}, 0);
        for (NovelRoom novel : participatingNovelRooms) {
            novelListTM.addRow(new Object[]{novel.getTitle(), novel.getDescription()});
        }

        // JTable 생성
        JTable novelListTable = new JTable(novelListTM){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        novelListTable.setFont(FontSetting.getInstance().loadCustomFont(16f)); // 폰트 설정
        novelListTable.setRowHeight(50);// 각 행의 높이 설정

        novelListTable.getTableHeader().setFont(FontSetting.getInstance().loadCustomFont(16f));
        novelListTable.getTableHeader().setReorderingAllowed(false);
        novelListTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        novelListTable.setGridColor(Color.LIGHT_GRAY);

        // DefaultTableCellRenderer로 가운데 정렬 설정
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // 모든 열에 대해 가운데 정렬 설정
        for (int i = 0; i < novelListTable.getColumnCount(); i++) {
            novelListTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

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
                        int roomId = participatingNovelRooms.get(row).getId();
                        handleNovelRoomClick(roomId, roomTitle);
                    }
                }
            }
        });

        // 스크롤 추가
        JScrollPane scrollPane = new JScrollPane(novelListTable);
        scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        // 패널에 추가
        JPanel participatingPanel = new JPanel(new BorderLayout());
        participatingPanel.add(scrollPane, BorderLayout.CENTER);

        DefaultListModel<String> novelListModel = new DefaultListModel<>();
        for (NovelRoom room : activeNovelRooms) {
            novelListModel.addElement("<html>" + room.getTitle() + "<br>" + room.getDescription() + "</html>");
        }

        contentLeftPanel.add(participatingLabelPanel);
        contentLeftPanel.add(participatingPanel);

        //============= 2. "활성화된 소설방" 섹션
        JPanel allRoomsLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel allRoomsLabel = new JLabel("활성화된 소설방");
        allRoomsLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        allRoomsLabelPanel.add(allRoomsLabel);
        allRoomsLabel.setFont(FontSetting.getInstance().loadCustomFont(20f));

        // 참여 중인 소설방 목록 데이터
        // DefaultTableModel 생성
        DefaultTableModel activeNovelListTM = new DefaultTableModel(new Object[]{"제목", "설명"}, 0);
        for (NovelRoom novel : activeNovelRooms) {
            activeNovelListTM.addRow(new Object[]{novel.getTitle(), novel.getDescription()});
        }

        // JTable 생성
        JTable activeNovelListTable = new JTable(activeNovelListTM){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };;
        activeNovelListTable.setFont(FontSetting.getInstance().loadCustomFont(16f)); // 폰트 설정
        activeNovelListTable.setRowHeight(50);// 각 행의 높이 설정

        activeNovelListTable.getTableHeader().setFont(FontSetting.getInstance().loadCustomFont(16f));
        activeNovelListTable.getTableHeader().setReorderingAllowed(false);
        activeNovelListTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        activeNovelListTable.setGridColor(Color.LIGHT_GRAY);

        // DefaultTableCellRenderer로 가운데 정렬 설정
        DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
        centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);

        // 모든 열에 대해 가운데 정렬 설정
        for (int i = 0; i < activeNovelListTable.getColumnCount(); i++) {
            activeNovelListTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
        }

        column1 = activeNovelListTable.getColumnModel().getColumn(0); // 첫 번째 열
        column1.setPreferredWidth(120); // 원하는 너비 설정
        column1.setMinWidth(120);       // 최소 너비 설정
        column1.setMaxWidth(120);       // 최대 너비 설정

        activeNovelListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastRequestTime < DEBOUNCE_TIME) {
                        return;
                    }
                    lastRequestTime = currentTime;

                    int row = activeNovelListTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String roomTitle = (String) activeNovelListTable.getValueAt(row, 0);
                        int roomId = activeNovelRooms.get(row).getId();
                        handleNovelRoomClick(roomId, roomTitle);
                    }
                }
            }
        });

        JScrollPane scrollPane2 = new JScrollPane(activeNovelListTable);
        scrollPane2.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        JPanel allRoomsPanel = new JPanel(new BorderLayout());
        allRoomsPanel.add(scrollPane2, BorderLayout.CENTER);

        contentRightPanel.add(allRoomsLabelPanel);
        contentRightPanel.add(allRoomsPanel);

        refreshPanels();
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

    private void handleNovelRoomClick(int roomId, String roomTitle) {
        try {
            ClientSenderThread.getInstance().requestRoomJoin(roomId);
        } catch (IllegalStateException ex) {
            CustomAlert.showAlert(this, "오류", "서버와 연결이 되어 있지 않습니다.", null);
            //JOptionPane.showMessageDialog(this, "서버와 연결이 되어 있지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initDatas() {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        List<NovelRoom> chatRoomsParticipating = dataModel.getChatRoomsParticipating();
        if (chatRoomsParticipating != null) {
            participatingNovelRooms = List.of(chatRoomsParticipating.toArray(new NovelRoom[0]));
        } else {
            participatingNovelRooms = List.of(new NovelRoom[0]); // null일 경우 빈 배열
        }
        List<NovelRoom> chatRoomsActive = dataModel.getChatRoomsActive();
        if (chatRoomsActive != null) {
            activeNovelRooms = List.of(chatRoomsActive.toArray(new NovelRoom[0]));
        } else {
            activeNovelRooms = List.of(new NovelRoom[0]);
        }

        // 테스트용 출력
        System.out.println("참여중인 소설방:");
        for (NovelRoom room : participatingNovelRooms) {
            System.out.println("- " + room.getTitle() + ": " + room.getDescription());
        }
        System.out.println("활성화된 소설방:");
        for (NovelRoom room : activeNovelRooms) {
            System.out.println("- " + room.getTitle() + ": " + room.getDescription());
        }
    }

    public void repaintHomeUI() {
        initDatas();
        showHome();
    }

    public static void main(String[] args) {
        new HomeUI();
    }
}
