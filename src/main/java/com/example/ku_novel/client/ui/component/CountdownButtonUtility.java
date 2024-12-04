package com.example.ku_novel.client.ui.component;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class CountdownButtonUtility {
    private int countdownTime;
    private JButton button;

    public CountdownButtonUtility(int initialTimeInSeconds, String buttonLabel) {
        this.countdownTime = initialTimeInSeconds;
        this.button = new RoundedButton(buttonLabel + "(" + formatTime(countdownTime) + ")", NovelColor.DARK_GREEN, Color.WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(120, 40));
    }

    public void startCountdown() {
        Timer timer = new Timer(1000, e -> {
            if (countdownTime > 0) {
                countdownTime--;
                button.setText("투표(" + formatTime(countdownTime) + ")");
            } else {
                ((Timer)e.getSource()).stop();  // 클라에선 타이머 중지
            }
        });
        timer.start();
    }

    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return new DecimalFormat("00").format(minutes) + ":" + new DecimalFormat("00").format(seconds);
    }

    public JButton getButton() {
        return button;
    }
}