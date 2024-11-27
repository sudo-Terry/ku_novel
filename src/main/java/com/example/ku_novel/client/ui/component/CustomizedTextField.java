package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CustomizedTextField extends JTextField {

    private String placeholder;
    private int arcWidth = 20;  // 모서리 둥글기 (가로)
    private int arcHeight = 20; // 모서리 둥글기 (세로)

    public CustomizedTextField(String placeholder) {
        this.placeholder = placeholder;

        // 기본 설정
        setOpaque(false); // 배경을 투명하게 설정
        setMargin(new Insets(5, 10, 5, 10)); // 내부 여백 설정 (위, 왼쪽, 아래, 오른쪽)
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티앨리어싱 활성화
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 필드 배경 그리기
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        // 플레이스홀더 텍스트 그리기
        if (getText().isEmpty() && !isFocusOwner()) {
            g2.setColor(Color.GRAY); // 플레이스홀더 텍스트 색상
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = getInsets().left; // 여백만큼 오른쪽으로 이동
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(placeholder, x, y);
        }

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 테두리 색상 및 둥근 테두리 그리기
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

        g2.dispose();
    }

    // 모서리 둥글기 설정 메서드
    public void setCornerRadius(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        repaint();
    }

    // 플레이스홀더 설정 메서드
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    // 플레이스홀더 반환 메서드
    public String getPlaceholder() {
        return placeholder;
    }
}

