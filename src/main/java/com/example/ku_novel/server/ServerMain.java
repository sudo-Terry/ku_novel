package com.example.ku_novel.server;

import com.example.ku_novel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

@Component
public class ServerMain implements CommandLineRunner {
    private static final int PORT = 10100;
    private final UserService userService;

    @Autowired
    public ServerMain(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception { // Spring Boot 애플리케이션 시작 시 실행
        start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            HashMap<String, PrintWriter> activeClients = new HashMap<>();
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // ClientHandler에 서비스 주입
                ClientHandler clientHandler = new ClientHandler(clientSocket, userService, activeClients);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class, args);
    }
}
