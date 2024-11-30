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

    // 데이터 갱신 디버깅용
    public String printData() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== ClientDataModel ===\n");
        sb.append("User ID: ").append(userId).append("\n");
        sb.append("Password: ").append(password).append("\n");
        sb.append("User Name: ").append(userName).append("\n");
        sb.append("User Points: ").append(userPoint).append("\n");

        sb.append("\n--- 활성화된 소설방 ---\n");
        if (chatRoomsActive != null) {
            for (NovelRoom room : chatRoomsActive) {
                sb.append(room).append("\n");
            }
        } else {
            sb.append("활성화된 소설방 없음\n");
        }

        sb.append("\n--- 참여중인 소설방 ---\n");
        if (chatRoomsParticipating != null) {
            for (NovelRoom room : chatRoomsParticipating) {
                sb.append(room).append("\n");
            }
        } else {
            sb.append("참여중인 소설방 없음.\n");
        }

        sb.append("\n--- 관심 소설방 ---\n");
        if (chatRoomsFavorite != null) {
            for (NovelRoom room : chatRoomsFavorite) {
                sb.append(room).append("\n");
            }
        } else {
            sb.append("관심 소설방 없음\n");
        }

        return sb.toString();
    }
}