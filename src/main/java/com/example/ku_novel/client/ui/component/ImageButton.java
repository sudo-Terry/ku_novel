package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import java.awt.*;

public class ImageButton extends JButton {

    private Color background;

    // 생성자
    public ImageButton(String imagePath, Color background) {
        this.background = background;

        // 이미지를 로드하고 버튼에 적용
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaledImage));

        // 버튼의 기본 스타일 설정
        setBorderPainted(false); // 테두리 제거
        setContentAreaFilled(false); // 버튼 배경 제거
        setFocusPainted(false); // 포커스 테두리 제거
        setOpaque(false); // 투명 배경

        // 둥근 모서리 설정
        setPreferredSize(new Dimension(60, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(new Color(220, 220, 220)); // 클릭 시 색상 변화
        } else {
            g.setColor(background); // 기본 배경색
        }

        // 둥근 모서리를 가진 버튼 배경 그리기
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 모서리 곡률 설정
        g2.dispose();

        super.paintComponent(g); // 버튼 기본 렌더링
    }

    public void setBackground(Color color) {
        this.background = color;
        repaint();
    }
}
