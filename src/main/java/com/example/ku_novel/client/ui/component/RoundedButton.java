package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class RoundedButton extends JButton {

    private int arcWidth = 20;  // 모서리의 가로 반경
    private int arcHeight = 20; // 모서리의 세로 반경

    private Color background;
    private Color foreground;

    private Color borderColor = Color.LIGHT_GRAY;

    public RoundedButton(String text, Color background, Color foreground) {
        super(text);
        this.background = background; // 배경 색
        this.foreground = foreground; // 글자 색
        setForeground(foreground);
        setContentAreaFilled(false); // 기본 배경 제거
        setFocusPainted(false);      // 포커스 테두리 제거
        setBorderPainted(false);     // 기본 경계 제거
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티앨리어싱 활성화
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 버튼 배경 색상
        g2.setColor(background);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        // 버튼 텍스트
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();
        int x = (getWidth() - stringWidth) / 2;
        int y = (getHeight() + stringHeight) / 2 - 2;
        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티앨리어싱 활성화
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 테두리 색상
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

        g2.dispose();
    }

    public void setCornerRadius(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }
}

