package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.connection.ClientSenderThread;
import com.example.ku_novel.client.model.ClientDataModel;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

@Getter
public class NovelRoomSettingsModalUI extends JDialog {
    private JTextField authorCountField;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox endNovelCheckbox;

    public NovelRoomSettingsModalUI() {
        setTitle("소설방 설정");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
        initDatas();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // 상단 제목
        JLabel titleLabel = new JLabel("소설방 설정 변경");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 중앙 내용
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(Color.WHITE);

        // 소설가 수
        JPanel authorPanel = createFieldPanel("소설가 수:", (field) -> authorCountField = field);

        // 소설 제목
        JPanel titlePanel = createFieldPanel("소설 제목:", (field) -> titleField = field);

        // 소설 설명
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBackground(Color.WHITE);
        JLabel descriptionLabel = new JLabel("소설 설명:");
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionPanel.add(descriptionLabel, BorderLayout.NORTH);
        descriptionPanel.add(descriptionScroll, BorderLayout.CENTER);

        // 소설 종료
        JPanel endNovelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        endNovelPanel.setBackground(Color.WHITE);
        JLabel endNovelLabel = new JLabel("소설 종료:");
        endNovelCheckbox = new JCheckBox();
        endNovelCheckbox.setBackground(Color.WHITE);
        endNovelPanel.add(endNovelLabel);
        endNovelPanel.add(endNovelCheckbox);

        contentPanel.add(authorPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(descriptionPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(endNovelPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(e -> saveSettings());
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createFieldPanel(String labelText, Consumer<JTextField> fieldConsumer) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField(15);
        fieldConsumer.accept(textField);
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private void initDatas() {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        authorCountField.setText(String.valueOf(dataModel.getNovelMaxParticipants()));
        titleField.setText(dataModel.getNovelRoomTitle());
        descriptionArea.setText(dataModel.getNovelRoomDescription());
    }

    private void saveSettings() {
        String novelAuthorCount = authorCountField.getText();
        String novelRoomTitle = titleField.getText();
        String novelRoomDescription = descriptionArea.getText();
        boolean isNovelEnded = endNovelCheckbox.isSelected();

        // 서버로 요청
        ClientSenderThread.getInstance().requestRoomStatusUpdate(
            Integer.parseInt(novelAuthorCount), novelRoomTitle, novelRoomDescription, isNovelEnded
        );

        this.dispose();
    }
}