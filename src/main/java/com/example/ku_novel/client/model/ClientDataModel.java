package com.example.ku_novel.client.model;

import com.example.ku_novel.domain.NovelRoom;
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

    private static volatile ClientDataModel instance;

    private ClientDataModel() {
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
}