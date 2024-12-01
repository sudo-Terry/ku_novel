package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        thumbColor = NovelColor.BLACK_GREEN; // 스크롤 손잡이 색상
        trackColor = NovelColor.DEFAULT; // 스크롤 트랙 색상
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton(); // 버튼 제거
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton(); // 버튼 제거
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}
