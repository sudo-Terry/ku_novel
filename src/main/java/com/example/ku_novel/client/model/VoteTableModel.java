package com.example.ku_novel.client.model;

import javax.swing.table.AbstractTableModel;

public class VoteTableModel extends AbstractTableModel {
    private final String[] columnNames = {"소설 내용", "선택"};
    private final Object[][] data = {
            {"이것은 소설 1의 내용입니다.", false},
            {"이것은 소설 2의 내용입니다.", false},
            {"이것은 소설 3의 내용입니다.", false}
    };

    private int selectedRow = -1; // 현재 선택된 행

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        return data[row][column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1; // "선택" 열만 편집 가능
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (column == 1) {
            // 이전 선택 해제
            if (selectedRow != -1) {
                data[selectedRow][column] = false;
            }
            // 새로운 선택
            data[row][column] = true;
            selectedRow = row;
            fireTableDataChanged(); // 테이블 갱신
        }
    }
}
