package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CircularImage extends JPanel {
    private BufferedImage image;

    public CircularImage(String imagePath) {
        try {
            // 이미지 로드
            image = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "이미지를 로드할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(NovelColor.BLACK_GREEN);

        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;

            // 앤티앨리어싱 활성화
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 패널 크기에 맞게 원형 영역 생성
            int size = Math.min(getWidth(), getHeight());
            Ellipse2D circle = new Ellipse2D.Double(0, 0, size, size);

            // 이미지 크롭
            BufferedImage croppedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = croppedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(circle);
            g2.drawImage(image, 0, 0, size, size, null);
            g2.dispose();

            // 원형 이미지 그리기
            g2d.drawImage(croppedImage, 0, 0, null);
        }
    }
}
