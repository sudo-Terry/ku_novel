package com.example.ku_novel.client.connection;

import com.example.ku_novel.common.Message;
import com.example.ku_novel.common.MessageType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSenderThread extends Thread{
    private final Socket socket;
    private PrintWriter writer = null;

    public ClientSenderThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void requestLogin(String id, String password){
        Message loginMessage = new Message();
        loginMessage.setType(MessageType.LOGIN);
        loginMessage.setSender(id);
        loginMessage.setPassword(password);

        writer.println(loginMessage.toJson());
    }

    public void requestSignUp(String id, String password, String userName){
        Message signUpMessage = new Message();
        signUpMessage.setType(MessageType.SIGNUP);
        signUpMessage.setSender(id);
        signUpMessage.setNickname(userName);
        signUpMessage.setPassword(password);

        writer.println(signUpMessage.toJson());
    }
}
