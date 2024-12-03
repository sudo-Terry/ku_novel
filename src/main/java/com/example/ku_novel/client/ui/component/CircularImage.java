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
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(NovelColor.BLACK_GREEN);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 패널 크기
        int size = Math.min(getWidth(), getHeight());
        Ellipse2D circle = new Ellipse2D.Double(0, 0, size, size);

        // 원형 배경을 흰색으로 채우기
        g2d.setColor(Color.WHITE);
        g2d.fill(circle);

        if (image != null) {
            // 이미지 비율 유지하며 크기 조정
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            double scale = Math.min((double) size / imageWidth, (double) size / imageHeight);
            int scaledWidth = (int) (imageWidth * scale);
            int scaledHeight = (int) (imageHeight * scale);

            // 중심에 이미지를 맞추기
            int x = (size - scaledWidth) / 2;
            int y = (size - scaledHeight) / 2;

            BufferedImage croppedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = croppedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(circle);

            g2.drawImage(image, x, y, scaledWidth, scaledHeight, null);
            g2.dispose();

            // 원형 이미지 그리기
            g2d.drawImage(croppedImage, 0, 0, null);
        }
    }

    public void changeImage(String path) {
        try {
            // 이미지 로드
            image = ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        repaint();
    }
}
