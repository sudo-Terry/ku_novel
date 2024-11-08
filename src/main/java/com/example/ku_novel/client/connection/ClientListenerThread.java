package com.example.ku_novel.client.connection;

import com.example.ku_novel.common.Message;
import com.example.ku_novel.common.MessageType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientListenerThread extends Thread {
    private final Socket socket;
    private final ConcurrentLinkedQueue<Message> messageQueue;
    private final Gson gson;

    public ClientListenerThread(Socket socket, ConcurrentLinkedQueue<Message> messageQueue) {
        this.socket = socket;
        this.messageQueue = messageQueue;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                JsonObject jsonObject = gson.fromJson(line, JsonObject.class);
                String messageType = jsonObject.has("messageType") ? jsonObject.get("messageType").getAsString() : "";

                System.out.println(line + "응답 도착");
                // 새 UI 컴포넌트 실행
                if (messageType.isEmpty()) {
                    throw new Exception();
                }else if (messageType.equals(MessageType.LOGIN_SUCCESS.toString())) {
                    // 로그인 성공 처리
                    System.out.println("로그인 성공");
                }else if (messageType.equals(MessageType.LOGIN_FAILED.toString())){
                    // 로그인 실패 처리
                    System.out.println("로그인 실패");
                } else{ // 메시지 큐에 추가해서 UI 컴포넌트 내에서 처리
                    Message message = gson.fromJson(jsonObject, Message.class);
                    messageQueue.add(message);
                    System.out.println(message + " 가 큐에 추가됨");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try{
                if (br != null) br.close();
                if (socket != null) socket.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}