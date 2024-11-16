package com.example.ku_novel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.ku_novel")
public class KuNovelApplication {
    public static void main(String[] args) {
        SpringApplication.run(KuNovelApplication.class, args);
    }
}