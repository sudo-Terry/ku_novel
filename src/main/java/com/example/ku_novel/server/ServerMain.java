package com.example.ku_novel.server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(10100);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트 연결 확인");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
