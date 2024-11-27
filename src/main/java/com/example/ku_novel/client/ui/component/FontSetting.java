package com.example.ku_novel.client.ui.component;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontSetting {
    private static FontSetting instance;

    public static FontSetting getInstance() {
        if (instance == null) {
            instance = new FontSetting();
        }
        return instance;
    }

    public Font loadCustomFont(float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/Pretendard-Medium.otf")).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

    public Font loadLogoFont(float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/font/logo.otf")).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("폰트를 로드하는 데 실패했습니다.");
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }
}
