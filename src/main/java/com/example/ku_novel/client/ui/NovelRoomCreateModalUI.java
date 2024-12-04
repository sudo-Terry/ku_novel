package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.component.*;

import javax.swing.*;
import java.awt.*;

public class NovelRoomCreateModalUI extends JDialog {
    private JTextField roomTitleField;
    private JTextField roomDescriptionField;
    private JSpinner novelistTimeSpinner;
    private JSpinner voteTimeSpinner;
    private JSpinner novelistCountSpinner;

    public NovelRoomCreateModalUI(JFrame parent) {
        super(parent, "소설방 생성", true);
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        Dimension fieldSize = new Dimension(250, 45);
        Dimension buttonSize = new Dimension(120, 45);
        Dimension spinnerSize = new Dimension(70, 30);

        // 소설방 생성 제목
        JPanel createPanel = new JPanel(new BorderLayout());
        createPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        createPanel.setBackground(Color.WHITE);
        createPanel.setPreferredSize(new Dimension(800, 150));

        JLabel createLabel = new JLabel("소설방 생성");
        createLabel.setFont(FontSetting.getInstance().loadCustomFont(48f));
        createLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel label = new JLabel("※ 방 생성: 500포인트 차감 (보유: "+ClientDataModel.getInstance().getUserPoint()+"포인트)");
        label.setFont(FontSetting.getInstance().loadCustomFont(16f));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        createPanel.add(label, BorderLayout.SOUTH);

        createPanel.add(createLabel, BorderLayout.CENTER);
        mainPanel.add(createPanel, BorderLayout.NORTH);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(5, 300));
        emptyPanel.setBackground(Color.WHITE);
        mainPanel.add(emptyPanel, BorderLayout.CENTER);

        // (왼쪽) 소설 설정: 소설 제목 + 설명
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(390, 300));
        //leftPanel.setBackground(Color.WHITE);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(10, 10, 18, 10);

        JLabel leftLabel = new JLabel("소설 설정");
        leftLabel.setFont(FontSetting.getInstance().loadCustomFont(20f));
        leftLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        leftLabel.setBackground(Color.WHITE);
        leftLabel.setOpaque(true);

        leftGbc.anchor = GridBagConstraints.CENTER;
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.gridwidth = 2;
        leftPanel.add(leftLabel, leftGbc);

        // 소설 제목
        JLabel titleLabel = new JLabel("소설 제목");
        titleLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));

        roomTitleField = new CustomizedTextField("소설 제목 입력");
        roomTitleField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        roomTitleField.setPreferredSize(fieldSize);

        leftGbc.anchor = GridBagConstraints.EAST;
        leftGbc.gridy = 1;
        leftGbc.gridwidth = 1;
        leftPanel.add(titleLabel, leftGbc);

        leftGbc.gridx = 1;
        leftGbc.anchor = GridBagConstraints.WEST;
        leftPanel.add(roomTitleField, leftGbc);

        // 소설방 설명
        JLabel descriptionLabel = new JLabel("소설 설명");
        descriptionLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));

        roomDescriptionField = new CustomizedTextField("소설 설명 입력");
        roomDescriptionField.setFont(FontSetting.getInstance().loadCustomFont(16f));
        roomDescriptionField.setPreferredSize(fieldSize);

        leftGbc.gridx = 0;
        leftGbc.gridy = 2;
        leftGbc.anchor = GridBagConstraints.EAST;
        leftPanel.add(descriptionLabel, leftGbc);

        leftGbc.gridx = 1;
        leftGbc.anchor = GridBagConstraints.WEST;
        leftPanel.add(roomDescriptionField, leftGbc);

        leftGbc.gridy = 3;
        leftGbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(new JPanel(), leftGbc);

        // (오른쪽) 소설 설정
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setPreferredSize(new Dimension(390, 300));
        //rightPanel.setBackground(Color.WHITE);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(10, 10, 20, 10);

        JLabel rightLabel = new JLabel("릴레이 방식 설정");
        rightLabel.setFont(FontSetting.getInstance().loadCustomFont(20f));
        rightLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        rightLabel.setBackground(Color.WHITE);
        rightLabel.setOpaque(true);

        rightGbc.anchor = GridBagConstraints.CENTER;
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.gridwidth = 3;
        rightPanel.add(rightLabel, rightGbc);

        // 소설가 입력 시간
        JLabel novelistTimeLabel = new JLabel("소설가 입력 시간 (분)");
        novelistTimeLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));

        novelistTimeSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 120, 1)); // 기본값 10, 최소 1, 최대 120, offset 1
        novelistTimeSpinner.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        novelistTimeSpinner.setPreferredSize(spinnerSize);
        novelistTimeSpinner.setFont(FontSetting.getInstance().loadCustomFont(16f));

        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightGbc.gridwidth = 2;
        rightGbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(novelistTimeLabel, rightGbc);

        rightGbc.gridx = 2;
        rightGbc.gridwidth = 1;
        rightGbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(novelistTimeSpinner, rightGbc);

        // 소설 투표 시간
        JLabel voteTimeLabel = new JLabel("소설 투표 시간 (분)");
        voteTimeLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));

        voteTimeSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1)); // 기본값 5, 최소 1, 최대 60, 증가 단위 1
        voteTimeSpinner.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        voteTimeSpinner.setPreferredSize(spinnerSize);
        voteTimeSpinner.setFont(FontSetting.getInstance().loadCustomFont(16f));

        rightGbc.gridx = 0;
        rightGbc.gridy = 2;
        rightGbc.gridwidth = 2;
        rightGbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(voteTimeLabel, rightGbc);

        rightGbc.gridx = 2;
        rightGbc.gridwidth = 1;
        rightGbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(voteTimeSpinner, rightGbc);

        // 소설가 수
        JLabel novelistCountLabel = new JLabel("소설가 수");
        novelistCountLabel.setFont(FontSetting.getInstance().loadCustomFont(16f));

        novelistCountSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1)); // 기본값 2, 최소 1, 최대 10
        novelistCountSpinner.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        novelistCountSpinner.setPreferredSize(spinnerSize);
        novelistCountSpinner.setFont(FontSetting.getInstance().loadCustomFont(16f));

        rightGbc.gridx = 0;
        rightGbc.gridy = 3;
        rightGbc.gridwidth = 2;
        rightGbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(novelistCountLabel, rightGbc);

        rightGbc.gridx = 2;
        rightGbc.gridwidth = 1;
        rightGbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(novelistCountSpinner, rightGbc);

        // 확인 버튼
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        buttonPanel.setPreferredSize(new Dimension(800,120));
        buttonPanel.setBackground(Color.WHITE);

        JButton createButton = new RoundedButton("생성", NovelColor.DARK_GREEN, Color.WHITE);
        createButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        createButton.setPreferredSize(buttonSize);
        createButton.addActionListener(e -> handleRoomCreation());
        buttonPanel.add(createButton);

        // 취소 버튼
        RoundedButton cancelButton = new RoundedButton("취소", Color.WHITE, Color.GRAY);
        cancelButton.setFont(FontSetting.getInstance().loadCustomFont(20f));
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    private void handleRoomCreation() {
        String roomTitle = roomTitleField.getText();
        String roomDescription = roomDescriptionField.getText();
        int submissionDuration = (int) novelistTimeSpinner.getValue();
        int voingDuration = (int) voteTimeSpinner.getValue();
        int novelistCount = (int) novelistCountSpinner.getValue(); // 소설가 수 값 가져오기

        // 유효성 검사
        if (roomTitle.isEmpty() || roomDescription.isEmpty()) {
            CustomAlert.showAlert(this, "오류", "모든 필드를 입력하세요.", null);
            //JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 서버에 소설방 생성 요청
            ClientSenderThread.getInstance().requestCreateNovelRoom(roomTitle, roomDescription, submissionDuration, voingDuration, novelistCount);
            dispose();
        } catch (Exception ex) {
            CustomAlert.showAlert(this, "오류", "소설방 생성 중 오류가 발생했습니다.", null);
            //JOptionPane.showMessageDialog(this, "소설방 생성 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}