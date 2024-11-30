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
    // HomeUI 데이터
    private String userId;
    private String password;
    private String userName;
    private String userPoint;
    private int currentRoomId;
    private List<NovelRoom> chatRoomsAll;
    private List<NovelRoom> chatRoomsActive;
    private List<NovelRoom> chatRoomsParticipating;
    private List<NovelRoom> chatRoomsFavorite;
    private Gson gson;

    // NovelRoomModalUI 데이터
    private String novelRoomTitle;
    private String novelRoomDescription;

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

    public void setChatRoomFromJson(JsonObject jsonObject) {
        try {
            JsonObject novelRoomObject = jsonObject.getAsJsonObject("novelRoom");
            if (novelRoomObject == null) {
                throw new IllegalArgumentException("novelRoom 객체가 없습니다.");
            }

            this.novelRoomTitle = novelRoomObject.get("novelRoomTitle").getAsString();
            this.novelRoomDescription = novelRoomObject.get("novelRoomDescription").getAsString();
            this.currentRoomId = novelRoomObject.get("novelRoomId").getAsInt();

            System.out.println("setChatRoomFromJson: 소설방 데이터 갱신 완료");
        } catch (Exception e) {
            System.err.println("setChatRoomFromJson 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
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