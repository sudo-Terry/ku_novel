package com.example.ku_novel.client.connection;

import java.io.*;
import java.net.Socket;

public class ClientListenerThread extends Thread {
    private Socket socket = null;

    public ClientListenerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader br = null;

        try{
            String line = null;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Listener 스레드 실행 확인");

            while ((line = br.readLine()) != null) {
                //전달받은 데이터 처리 부분
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try{
                if (br != null) br.close();
                if (socket != null) socket.close();
            }catch (Exception e){}
        }
    }
}