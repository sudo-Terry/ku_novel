package com.example.ku_novel.client.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientDataModel {
    private String userId;
    private String password;
    private String userName;
    private String userPoint;
    private List<String> chatRoomsActive;

    private static volatile ClientDataModel instance;

    private ClientDataModel() {
        chatRoomsActive = new ArrayList<>();
    }

    public static ClientDataModel getInstance() {
        if (instance == null) { // 첫 번째 체크
            synchronized (ClientDataModel.class) {
                if (instance == null) { // 두 번째 체크
                    instance = new ClientDataModel();
                }
            }
        }
        return instance;
    }

    // ==== lombok에서는 ThreadSafe getter setter을 지원하지 않는 듯? ====

    public synchronized void addChatRoom(String chatRoom) {
        if (!chatRoomsActive.contains(chatRoom)) {
            chatRoomsActive.add(chatRoom);
        }
    }

    public synchronized List<String> getChatRoomsActive() {
        return new ArrayList<>(chatRoomsActive);
    }

    public synchronized void removeChatRoom(String chatRoom) {
        chatRoomsActive.remove(chatRoom);
    }
}