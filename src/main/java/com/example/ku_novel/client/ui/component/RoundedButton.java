package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private int arcWidth = 20;  // 모서리의 가로 반경
    private int arcHeight = 20; // 모서리의 세로 반경

    private Color background;
    private Color foreground;
    private Color borderColor = Color.LIGHT_GRAY;

    private Color hoverBackground;   // 마우스 오버 배경색
    private Color clickBackground;  // 클릭 배경색

    private boolean hovered = false;  // 마우스 오버 상태
    private boolean pressed = false; // 클릭 상태
    private boolean colorChangeEnabled = true; // 색상 변화 활성화 여부

    public RoundedButton(String text, Color background, Color foreground) {
        super(text);
        this.background = background;
        this.foreground = foreground;
        this.hoverBackground = background.brighter(); // 기본 hover 색상
        this.clickBackground = background.darker();   // 기본 클릭 색상
        setForeground(foreground);
        setContentAreaFilled(false); // 기본 배경 제거
        setFocusPainted(false);      // 포커스 테두리 제거
        setBorderPainted(false);     // 기본 경계 제거
        addListeners();
    }

    public RoundedButton(String text, Color background, Color foreground, Color borderColor) {
        this(text, background, foreground);
        this.borderColor = borderColor;
    }

    private void addListeners() {
        // 마우스 이벤트 처리
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (colorChangeEnabled) {
                    hovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (colorChangeEnabled) {
                    hovered = false;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (colorChangeEnabled) {
                    pressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (colorChangeEnabled) {
                    pressed = false;
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티앨리어싱 활성화
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 버튼 배경색 상태별 설정
        if (colorChangeEnabled && pressed) {
            g2.setColor(clickBackground); // 클릭 상태
        } else if (colorChangeEnabled && hovered) {
            g2.setColor(background.equals(Color.WHITE) ? Color.LIGHT_GRAY : hoverBackground);
        } else {
            g2.setColor(background); // 기본 상태
        }

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

    public void setHoverBackground(Color hoverBackground) {
        this.hoverBackground = hoverBackground;
    }

    public void setClickBackground(Color clickBackground) {
        this.clickBackground = clickBackground;
    }

    @Override
    public void setBackground(Color background) {
        this.background = background;
        this.hoverBackground = background.brighter(); // 기본 hover 색상 업데이트
        this.clickBackground = background.darker();   // 기본 클릭 색상 업데이트
        repaint();
    }

    // 색상 변화 활성화/비활성화 설정
    public void setColorChangeEnabled(boolean enabled) {
        this.colorChangeEnabled = enabled;
        repaint();
    }

    public boolean isColorChangeEnabled() {
        return colorChangeEnabled;
    }
}
