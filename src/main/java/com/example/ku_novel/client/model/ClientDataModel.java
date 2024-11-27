package com.example.ku_novel.client.model;

import com.example.ku_novel.common.Message;
import com.example.ku_novel.domain.NovelRoom;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    private long currentRoomId;
    private List<NovelRoom> chatRoomsAll;
    private List<NovelRoom> chatRoomsActive;
    private List<NovelRoom> chatRoomsParticipating;
    private List<NovelRoom> chatRoomsFavorite;
    private Gson gson;

    private static volatile ClientDataModel instance;

    private ClientDataModel() {
        gson = new Gson();
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

    public void setChatRoomsFromJson(JsonObject jsonObject) {
        // 진행중인 소설방 데이터
        Message[] activeMessages = gson.fromJson(jsonObject.get("activeNovelRooms"), Message[].class);
        List<NovelRoom> activeRooms = new ArrayList<>();
        for (Message message : activeMessages) {
            activeRooms.add(message.toNovelRoom());
        }
        chatRoomsActive = activeRooms;

        // 참여중인 소설방 데이터
        Message[] participatingMessages = gson.fromJson(jsonObject.get("participatingNovelRooms"), Message[].class);
        List<NovelRoom> participatingRooms = new ArrayList<>();
        for (Message message : participatingMessages) {
            participatingRooms.add(message.toNovelRoom());
        }
        chatRoomsParticipating = participatingRooms;
    }
}