package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageButton extends JButton {

    private Color background;
    private Color hoverBackground;
    private Color clickBackground;
    private boolean hovered = false;

    // 생성자
    public ImageButton(String imagePath, Color background) {
        this.background = background;
        this.hoverBackground = background.brighter();
        this.clickBackground = background.darker();

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

        // 마우스 이벤트 처리
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) { // 색상 변화 활성화 및 버튼 활성화 확인
                    hovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) { // 색상 변화 활성화 및 버튼 활성화 확인
                    hovered = false;
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        if (!isEnabled()) {
            g2.setColor(background);
        } else if (getModel().isPressed()) {
            g2.setColor(clickBackground);
        } else if (hovered) {
            g2.setColor(background.equals(Color.WHITE) ? Color.LIGHT_GRAY : hoverBackground);
        } else {
            g2.setColor(background);
        }

        // 둥근 모서리를 가진 버튼 배경 그리기
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();

        super.paintComponent(g); // 버튼 기본 렌더링
    }

    // 배경 색상 설정
    @Override
    public void setBackground(Color color) {
        this.background = color;
        this.hoverBackground = color.brighter();
        this.clickBackground = color.darker();
        repaint();
    }

}
