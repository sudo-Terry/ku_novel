package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.model.VoteTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class VoteModalUI extends JDialog {
    private static final int TIME_LIMIT_SECONDS = 10; // 제한 시간 (초 단위)

    public VoteModalUI(NovelRoomModalUI parent) {
        super(parent, "다음 내용 선택 투표", true);
        setLayout(new GridBagLayout());
        setSize(800, 600);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 10, 20);

        // 제목 라벨
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel preLabel = new JLabel("다음 소설 내용 투표");
        preLabel.setFont(loadCustomFont(16f));
        add(preLabel, gbc);

        // 타이머
        gbc.insets = new Insets(10, 20, 10, 20);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel timerLabel = new JLabel("남은 시간 표시");
        timerLabel.setFont(loadCustomFont(16f));
        add(timerLabel, gbc);

        // 이전 소설 내용
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 2.0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;

        VoteTableModel model = new VoteTableModel();

        // JTable 생성
        JTable table = new JTable(model);
        table.setFont(loadCustomFont(16f)); // 폰트 설정
        table.setRowHeight(50);// 각 행의 높이 설정

        // 체크박스 렌더러 및 에디터 설정
        table.getColumn("선택").setCellRenderer(new RadioButtonRenderer());
        table.getColumn("선택").setCellEditor(new RadioButtonEditor(model));

        TableColumn column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(120); // 원하는 너비 설정
        column.setMinWidth(120);       // 최소 너비 설정
        column.setMaxWidth(120);       // 최대 너비 설정

        add(new JScrollPane(table), gbc);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("투표");
        okButton.setFont(loadCustomFont(16f));
        JButton cancelButton = new JButton("취소");
        cancelButton.setFont(loadCustomFont(16f));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // 확인 버튼 동작
        okButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "등록 완료");
            dispose();
        });

        // 취소 버튼 동작
        cancelButton.addActionListener(e -> {
            dispose();
        });

        // 버튼 패널 추가
        gbc.insets = new Insets(10, 20, 20, 20);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // JDialog 설정
        setLocationRelativeTo(parent);
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

    // 체크박스 렌더러
    static class RadioButtonRenderer extends JRadioButton implements TableCellRenderer {
        public RadioButtonRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected((Boolean) value);
            return this;
        }
    }

    // 체크박스 에디터
    static class RadioButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JRadioButton radioButton;
        private final VoteTableModel model;
        private int row;

        public RadioButtonEditor(VoteTableModel model) {
            this.model = model;
            radioButton = new JRadioButton();
            radioButton.setHorizontalAlignment(SwingConstants.CENTER);

            // 선택 시 데이터 업데이트
            radioButton.addActionListener(e -> {
                model.setValueAt(true, row, 1);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            radioButton.setSelected((Boolean) value);
            return radioButton;
        }

        @Override
        public Object getCellEditorValue() {
            return radioButton.isSelected();
        }
    }
}

