package com.example.ku_novel.client.ui;

import com.example.ku_novel.client.model.VoteTableModel;
import com.example.ku_novel.client.ui.component.FontSetting;
import com.example.ku_novel.client.ui.component.NovelColor;
import com.example.ku_novel.client.ui.component.RoundedButton;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class VoteModalUI extends JDialog {
    private static final int TIME_LIMIT_SECONDS = 10; // 제한 시간 (초 단위)

    public VoteModalUI(NovelRoomModalUI parent) {
        super(parent, "투표", true);
        setSize(800, 600);

        // 메인 패널
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // 상단 제목 라벨
        JLabel preLabel = new JLabel("다음 소설 내용 선택", SwingConstants.CENTER);
        preLabel.setFont(FontSetting.getInstance().loadCustomFont(28f));
        preLabel.setBackground(Color.WHITE);
        preLabel.setOpaque(true);
        mainPanel.add(preLabel, BorderLayout.NORTH);

        // 테이블 모델 생성
        VoteTableModel model = new VoteTableModel();

        // JTable 생성
        JTable table = new JTable(model);
        table.setFont(FontSetting.getInstance().loadCustomFont(16f)); // 폰트 설정
        table.setRowHeight(50); // 행 높이 설정
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);

        table.getTableHeader().setFont(FontSetting.getInstance().loadCustomFont(16f));
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        table.getTableHeader().setBackground(NovelColor.DARK_GREEN);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        // 체크박스 렌더러 및 에디터 설정
        table.getColumn("선택").setCellRenderer(new RadioButtonRenderer());
        table.getColumn("선택").setCellEditor(new RadioButtonEditor(model));

        TableColumn column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(120); // 원하는 너비 설정
        column.setMinWidth(120);       // 최소 너비 설정
        column.setMaxWidth(120);       // 최대 너비 설정

        // JScrollPane으로 JTable 추가
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(800, 50));

        JButton okButton = new RoundedButton("투표(10:00)", NovelColor.DARK_GREEN, Color.WHITE);
        okButton.setPreferredSize(new Dimension(100, 40));
        okButton.setFont(FontSetting.getInstance().loadCustomFont(16f));

        JButton cancelButton = new RoundedButton("취소", Color.WHITE, Color.DARK_GRAY);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setFont(FontSetting.getInstance().loadCustomFont(16f));

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // 확인 버튼 동작
        okButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "등록 완료");
            dispose();
        });

        // 취소 버튼 동작
        cancelButton.addActionListener(e -> dispose());

        // 버튼 패널 추가
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // JDialog 설정
        setLocationRelativeTo(parent);
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
