package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.component.*;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class NovelRoomModalUI extends JDialog {
    private static NovelRoomModalUI instance;
    @Getter
    private int roomId;
    private String roomTitle;
    private String roomDescription;
    private StyledDocument doc;
    RoundedButton participantButton;

    private Boolean isClosed = false;

    JPanel mainPanel, topPanel, bottomButtonPanel;

    JTextArea chatTextArea;

    JTextPane novelTextPane;

    private NovelRoomModalUI() {
        setTitle("개별 소설방");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ClientSenderThread.getInstance().requestRoomLeave(
                    ClientDataModel.getInstance().getUserId(),
                    ClientDataModel.getInstance().getCurrentRoomId()
                );
            }
        });
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static NovelRoomModalUI getInstance() {
        if (instance == null) {
            synchronized (NovelRoomModalUI.class) {
                if (instance == null) {
                    instance = new NovelRoomModalUI();
                }
            }
        }
        return instance;
    }

    public void openModalWithRoomId(int roomId) {
        this.roomId = roomId;
        initDatas();
        initUI();
        this.setVisible(true);
    }

    private void initDatas(){
        ClientDataModel dataModel = ClientDataModel.getInstance();

        this.roomTitle = dataModel.getNovelRoomTitle();
        setTitle(roomTitle);
        this.roomDescription = dataModel.getNovelRoomDescription();
    }

    // UI 초기화
    private void initUI() {
        getContentPane().removeAll(); // 기존 UI 제거
        repaint();
        revalidate();

        //============= 메인 패널
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        //============= 상단 패널
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topPanel.setPreferredSize(new Dimension(1080, 100));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        topPanel.setBackground(Color.WHITE);

        JPanel novelInfoPanel = new JPanel();
        novelInfoPanel.setLayout(new BoxLayout(novelInfoPanel, BoxLayout.Y_AXIS));
        novelInfoPanel.setPreferredSize(new Dimension(820, 100));
        novelInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        novelInfoPanel.setOpaque(false);

        // 소설 제목
        JLabel titleLabel = new JLabel(roomTitle);
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(32f));
        novelInfoPanel.add(titleLabel);

        // 소설 설명
        JLabel descriptionLabel = new JLabel(": " + roomDescription);
        descriptionLabel.setFont(FontSetting.getInstance().loadCustomFont(20f));
        novelInfoPanel.add(descriptionLabel);
        topPanel.add(novelInfoPanel);

        // 소설 참여 인원 수
        JPanel participantPanel = new JPanel(new BorderLayout());
        participantPanel.setBackground(Color.WHITE);
        JLabel image = new JLabel("현재 독자 수", scaleIcon("src/main/resources/icon/eyes.png", 20, 20), JLabel.CENTER);
        image.setFont(FontSetting.getInstance().loadCustomFont(14f));
        participantButton = new RoundedButton(
                Integer.toString(ClientDataModel.getInstance().getParticipantsCount()),
                NovelColor.LIGHT_GREEN,
                Color.WHITE
        );
        participantButton.setColorChangeEnabled(false);
        participantButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        participantButton.setPreferredSize(new Dimension(110, 40));
        participantPanel.add(image, BorderLayout.SOUTH);
        participantPanel.add(participantButton, BorderLayout.CENTER);

        topPanel.add(participantPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        //============= 콘텐츠 패널
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        contentPanel.setBackground(Color.WHITE);

        //============= 1. "소설" 섹션
        JPanel novelPanel = new JPanel();
        novelPanel.setLayout(new BoxLayout(novelPanel, BoxLayout.Y_AXIS));
        novelPanel.setBackground(Color.WHITE);

        JPanel novelTitlePanel = new JPanel();
        novelTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        novelTitlePanel.setMaximumSize(new Dimension(120, 30));
        novelTitlePanel.setBackground(Color.WHITE);
        JLabel novelTitleLabel = new JLabel("소설");
        novelTitleLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));
        novelTitlePanel.add(novelTitleLabel);
        novelPanel.add(novelTitlePanel);

        // 소설 내용
        novelTextPane = new JTextPane();
        novelTextPane.setFont(FontSetting.getInstance().loadCustomFont(20f));
        novelTextPane.setBorder(null);
        novelTextPane.setEditable(false);

        // 텍스트 여백 설정
        novelTextPane.setMargin(new Insets(10, 10, 10, 10)); // 상, 좌, 하, 우 여백

        // 텍스트 스타일 설정
        doc = novelTextPane.getStyledDocument();
        SimpleAttributeSet paragraphAttributes = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(paragraphAttributes, 0.2f); // 줄 간격
        StyleConstants.setLeftIndent(paragraphAttributes, 10);    // 왼쪽 들여쓰기
        StyleConstants.setRightIndent(paragraphAttributes, 10);   // 오른쪽 들여쓰기
        StyleConstants.setSpaceAbove(paragraphAttributes, 10);    // 문단 위 간격
        StyleConstants.setSpaceBelow(paragraphAttributes, 10);    // 문단 아래 간격
        doc.setParagraphAttributes(0, doc.getLength(), paragraphAttributes, false);

        // 스크롤 추가
        JScrollPane novelScrollPane = new JScrollPane(novelTextPane);
        novelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        novelScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        novelScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        // 스크롤 뷰포트 여백 (옵션)
        // 뷰포트에 여백을 적용하기 위해 JTextPane 자체에 EmptyBorder를 추가
        novelTextPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 상, 좌, 하, 우 여백

        // 자동 스크롤 활성화
        DefaultCaret novelCaret = (DefaultCaret) novelTextPane.getCaret();
        novelCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // 소설 내용 추가
        try {
            doc.insertString(
                    doc.getLength(),
                    ClientDataModel.getInstance().getNovelContent() + '\n',
                    null
            );
            if(!ClientDataModel.getInstance().getNovelRoomStatus().equals("ACTIVE")){
                doc.insertString(
                        doc.getLength(),
                        "==== 소설이 종료되었습니다. ====" + '\n',
                        null
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 패널에 추가
        novelPanel.add(novelScrollPane);

        contentPanel.add(novelPanel);

        //============= 2. "채팅" 섹션
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        JPanel chatTitlePanel = new JPanel();
        chatTitlePanel.setBackground(Color.WHITE);
        chatTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel chatTitleLabel = new JLabel("채팅");
        chatTitleLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));
        chatTitlePanel.add(chatTitleLabel);
        chatPanel.add(chatTitlePanel);

        // 채팅
        chatTextArea = new JTextArea(20, 40);
        chatTextArea.setFont(FontSetting.getInstance().loadCustomFont(20f));
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true); // 줄바꿈 활성화
        chatTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 상, 좌, 하, 우 여백

        // 스크롤을 추가
        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
        novelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        chatScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        // 자동 스크롤 활성화
        DefaultCaret chatCaret = (DefaultCaret) chatTextArea.getCaret();
        chatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // 채팅 입력
        JTextField inputField = new CustomizedTextField("");
        inputField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        inputField.setPreferredSize(new Dimension(120, 45));

        JButton sendButton = new RoundedButton("전송", NovelColor.DARK_GREEN, Color.WHITE);
        sendButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        sendButton.setPreferredSize(new Dimension(70, 45));
        sendButton.addActionListener(e -> {
            String inputText = inputField.getText().trim();
            if (!inputText.isEmpty()) {
                ClientSenderThread.getInstance().requestNovelRoomMessageSend(
                        ClientDataModel.getInstance().getUserId(),
                        ClientDataModel.getInstance().getCurrentRoomId(),
                        inputText
                );
                inputField.setText(""); // 입력 필드 초기화
            }
        });
        if(!ClientDataModel.getInstance().getNovelRoomStatus().equals("ACTIVE")){
            sendButton.setEnabled(false);
        }

        // 엔터키 입력해도 채팅 입력되게
        inputField.addActionListener(e -> sendButton.doClick());

        // 하단 패널
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        //chatPanel.add(bestChatScrollPane);
        chatPanel.add(chatScrollPane);
        chatPanel.add(inputPanel);

        contentPanel.add(chatPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        //=========== 하단 버튼 패널
        initBottomPanel();
        topPanel.add(bottomButtonPanel);
        mainPanel.add(bottomButtonPanel,BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initBottomPanel() {
        bottomButtonPanel = new JPanel(new GridBagLayout());
        bottomButtonPanel.setPreferredSize(new Dimension(250, 90));
        bottomButtonPanel.setBackground(NovelColor.BLACK_GREEN);

        GridBagConstraints bottomGbc = new GridBagConstraints();
        bottomGbc.insets = new Insets(5, 15, 0, 15);

        // 소설방 설정 버튼
        JButton settingButton = new ImageButton("src/main/resources/icon/setting.png", Color.WHITE);

        JLabel settingLabel = new JLabel("설정");
        settingLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        settingLabel.setForeground(Color.WHITE);

        // 방장만 소설방 설정 변경
        if(!ClientDataModel.getInstance().getUserId().equals(ClientDataModel.getInstance().getHostUserId())
        || !ClientDataModel.getInstance().getNovelRoomStatus().equals("ACTIVE")){
            settingButton.setBackground(NovelColor.BLACK_GREEN);
            settingButton.setEnabled(false);
            settingLabel.setForeground(NovelColor.BLACK_GREEN);
        }

        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIHandler.getInstance().showNovelRoomSettingsModalUI(NovelRoomModalUI.this);
            }
        });

        bottomGbc.gridx = 0;
        bottomGbc.gridy = 0;
        bottomButtonPanel.add(settingButton, bottomGbc);

        bottomGbc.gridy = 1;
        bottomButtonPanel.add(settingLabel, bottomGbc);

        // 소설가 신청 버튼
        ImageButton applyAuthorButton = new ImageButton("src/main/resources/icon/hand.png", Color.WHITE);
        applyAuthorButton.addActionListener(e -> ClientSenderThread.getInstance().requestAuthorApply(
                ClientDataModel.getInstance().getUserId(),
                ClientDataModel.getInstance().getCurrentRoomId()
        ));
        applyAuthorButton.setEnabled(true);

        JLabel applyAuthorLabel = new JLabel("소설가 신청");
        applyAuthorLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        applyAuthorLabel.setForeground(Color.WHITE);

        // 소설가는 소설가 신청 버튼 비활성화
        for(String str : ClientDataModel.getInstance().getNovelParticipantIds()) {
            if (str.equals(ClientDataModel.getInstance().getUserId())) {
                applyAuthorLabel.setForeground(NovelColor.BLACK_GREEN);
                applyAuthorButton.setBackground(NovelColor.BLACK_GREEN);
                applyAuthorButton.setEnabled(false);
            }
        }
        if(!ClientDataModel.getInstance().getNovelRoomStatus().equals("ACTIVE")){
            applyAuthorLabel.setForeground(NovelColor.BLACK_GREEN);
            applyAuthorButton.setBackground(NovelColor.BLACK_GREEN);
            applyAuthorButton.setEnabled(false);
        }

        bottomGbc.gridx = 1;
        bottomGbc.gridy = 0;
        bottomButtonPanel.add(applyAuthorButton, bottomGbc);

        bottomGbc.gridy = 1;
        bottomButtonPanel.add(applyAuthorLabel, bottomGbc);

        // 현재 소설가 버튼
        JButton authorButton = new ImageButton("src/main/resources/icon/people.png", Color.WHITE);
        authorButton.addActionListener(e -> {
            String participantIds = ClientDataModel.getInstance().getNovelParticipantIds().toString();
            UIHandler.getInstance().showAlertModal(
                    this, "정보", "현재 소설가는 " + participantIds + " 입니다.", JOptionPane.INFORMATION_MESSAGE);
        });
        bottomGbc.gridx = 2;
        bottomGbc.gridy = 0;
        bottomButtonPanel.add(authorButton, bottomGbc);

        JLabel authorLabel = new JLabel("소설가 목록");
        authorLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        authorLabel.setForeground(Color.WHITE);
        bottomGbc.gridy = 1;
        bottomButtonPanel.add(authorLabel, bottomGbc);

        if(!ClientDataModel.getInstance().getNovelRoomStatus().equals("ACTIVE")){
            authorButton.setBackground(NovelColor.BLACK_GREEN);
            authorButton.setEnabled(false);
            authorLabel.setForeground(NovelColor.BLACK_GREEN);
        }

        // 투표 버튼
        JButton voteButton = new ImageButton("src/main/resources/icon/vote.png", Color.WHITE);
        voteButton.addActionListener(e-> {
            ClientSenderThread.getInstance().requestVoteFetchByID(
                    ClientDataModel.getInstance().getNovelVoteId()
            );
        });

        bottomGbc.gridx = 3;
        bottomGbc.gridy = 0;
        bottomButtonPanel.add(voteButton, bottomGbc);

        JLabel voteLabel = new JLabel("투표");
        voteLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        voteLabel.setForeground(Color.WHITE);
        bottomGbc.gridy = 1;
        bottomButtonPanel.add(voteLabel, bottomGbc);

        if(!ClientDataModel.getInstance().getNovelRoomStatus().equals("ACTIVE")){
            voteButton.setBackground(NovelColor.BLACK_GREEN);
            voteButton.setEnabled(false);
            voteLabel.setForeground(NovelColor.BLACK_GREEN);
        }

        // 소설 저장 버튼
        bottomGbc.gridx = 5;
        bottomGbc.gridy = 0;
        bottomGbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new ImageButton("src/main/resources/icon/download.png", NovelColor.BLACK_GREEN);
        saveButton.setEnabled(false);
        bottomButtonPanel.add(saveButton, bottomGbc);

        JLabel saveLabel = new JLabel("다운로드");
        saveLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        saveLabel.setForeground(NovelColor.BLACK_GREEN);
        bottomGbc.gridy = 1;
        bottomButtonPanel.add(saveLabel, bottomGbc);

        if(!ClientDataModel.getInstance().getNovelRoomStatus().equals("ACTIVE")) {
            saveButton.setBackground(Color.WHITE);
            saveButton.setEnabled(true);
            saveLabel.setForeground(Color.WHITE);
        }
        saveButton.addActionListener(e->download());

        // 소설 작성 버튼
        ImageButton writeButton = new ImageButton("src/main/resources/icon/writing.png", NovelColor.BLACK_GREEN);
        writeButton.setEnabled(false);

        bottomGbc.gridx = 6;
        bottomGbc.gridy = 0;
        bottomButtonPanel.add(writeButton, bottomGbc);

        JLabel writeLabel = new JLabel("소설 작성");
        writeLabel.setFont(FontSetting.getInstance().loadCustomFont(14f));
        bottomGbc.gridy = 1;
        writeLabel.setForeground(NovelColor.BLACK_GREEN);

        // 소설가만 소설작성 버튼 활성화
        for(String str : ClientDataModel.getInstance().getNovelParticipantIds()) {
            if (str.equals(ClientDataModel.getInstance().getUserId()) && ClientDataModel.getInstance().getNovelRoomStatus().equals("ACTIVE")) {
                writeButton.setBackground(Color.WHITE);
                writeButton.setEnabled(true);
                writeLabel.setForeground(Color.WHITE);
            }
        }

        writeButton.addActionListener(e-> {
            ClientSenderThread.getInstance().requestNovelFetchByID(
                    ClientDataModel.getInstance().getNovelVoteId()
            );
        });
        bottomButtonPanel.add(writeLabel, bottomGbc);

    }

    private void download() {
        // 파일 저장 대화상자 열기
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("소설 파일로 저장");

        // 기본 파일 이름 설정
        fileChooser.setSelectedFile(new java.io.File(roomTitle+".txt"));

        // 파일 필터 추가
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files (*.txt)", "txt"));


        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                // 사용자가 확장자를 입력하지 않은 경우 자동으로 .txt 추가
                String filePath = fileChooser.getSelectedFile().getPath();
                if (!filePath.toLowerCase().endsWith(".txt")) {
                    filePath += ".txt";
                }

                // 파일 저장
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(novelTextPane.getText());
                    CustomAlert.showAlert(this, "파일 다운로드", "파일 저장에 성공했습니다.", null);
                }
            } catch (IOException ex) {
                CustomAlert.showAlert(this, "파일 다운로드", "파일 저장에 실패했습니다.", null);
            }
        }
    }

    private ImageIcon scaleIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void updateChatArea(String formattedChat){
        chatTextArea.append(formattedChat + '\n');
    }

    public void updateUI(){
        this.roomTitle = ClientDataModel.getInstance().getNovelRoomTitle();
        this.roomDescription = ClientDataModel.getInstance().getNovelRoomDescription();
        initUI();
    }

    public void updateNovelContentArea(String newNovelContent){
        try {
            doc.insertString(
                    doc.getLength(),
                    newNovelContent + '\n',
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateParticipantButton(){
        if(participantButton != null) {
            participantButton.setText(String.valueOf(ClientDataModel.getInstance().getParticipantsCount()));
            participantButton.repaint();
            participantButton.revalidate();
        }
    }
}