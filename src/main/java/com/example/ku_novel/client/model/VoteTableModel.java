package com.example.ku_novel.client.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class VoteTableModel extends AbstractTableModel {
    private final String[] columnNames = {"소설 내용", "선택"};
    private final List<Object[]> data = new ArrayList<>();
    private int selectedRow = -1; // 현재 선택된 행

    @Override
    public int getRowCount() {
        return data.size();
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
        return data.get(row)[column];
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
                data.get(selectedRow)[1] = false;
            }
            data.get(row)[1] = true;
            selectedRow = row;
            fireTableDataChanged(); // 테이블 갱신
        }
    }

    public void addRow(String content) {
        data.add(new Object[]{content, false});
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    public void clear() {
        data.clear();
        selectedRow = -1;
        fireTableDataChanged();
    }
}