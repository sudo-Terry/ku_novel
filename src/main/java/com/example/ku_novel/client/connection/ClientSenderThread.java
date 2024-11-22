package com.example.ku_novel.client.connection;

import com.example.ku_novel.client.ui.UIHandler;
import com.example.ku_novel.common.Message;
import com.example.ku_novel.common.MessageType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSenderThread extends Thread{
    private static ClientSenderThread instance;
    private final Socket socket;
    private PrintWriter writer = null;

    private ClientSenderThread(Socket socket) {
        this.socket = socket;
    }

    public static synchronized ClientSenderThread initialize(Socket socket) {
        if (instance == null) {
            instance = new ClientSenderThread(socket);
        } else {
            throw new IllegalStateException("ClientSenderThread 가 이미 존재합니다.");
        }
        return instance;
    }

    public static synchronized ClientSenderThread getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ClientSenderThread 가 존재하지 않습니다.");
        }
        return instance;
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

    public void requestIdValidation(String id){
        Message idValidationMessage = new Message();
        idValidationMessage.setType(MessageType.ID_CHECK);
        idValidationMessage.setSender(id);

        writer.println(idValidationMessage.toJson());
    }

    public void requestNicknameValidation(String nickname){
        Message nicknameValidationMessage = new Message();
        nicknameValidationMessage.setType(MessageType.NICKNAME_CHECK);
        nicknameValidationMessage.setNickname(nickname);

        writer.println(nicknameValidationMessage.toJson());
    }

    public void requestRoomFetchActive(){
        Message roomListMessage = new Message();
        roomListMessage.setType(MessageType.ROOM_FETCH_ACTIVE);

        writer.println(roomListMessage.toJson());
    }
}
