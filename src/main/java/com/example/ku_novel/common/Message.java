package com.example.ku_novel.common;

import com.example.ku_novel.domain.NovelRoom;
import com.example.ku_novel.domain.Vote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Message {

    // Getters
    @Getter
    private MessageType type;
    @Getter
    private String sender;
    private String receiver;
    private String content;
    private String timestamp;
    private String password;
    private String nickname;
    private int point;

    private String json;

    private Message novelRoom;
    private List<Message> allNovelRooms;
    private List<Message> activeNovelRooms;
    private List<Message> participatingNovelRooms;

    /// 소설방 관련
    private int novelRoomId;
    private String novelRoomTitle;
    private String novelRoomDescription;
    private String novelRoomStatus;
    private int novelVoteId;
    private String novelHostUser;
    private List<String> novelParticipantIds;
    private String novelContent;
    private int votingDuration;
    private int submissionDuration;
    private int maxParticipants;
    private int voteChoice;
    private int participantsCount;
    private String novelEnded; // Lombok에서 is 접두사가 붙은 이름을 boolean getter로 간주해서 네이밍을 이렇게 하였음
//    private boolean isParticipating; // 안씀

    /// 투표 관련
    private Message vote; // 투표 정보
    private List<String> contentOptions;
    private String voteStatus; // WRITER_ENABLED || VOTING_ENABLED || VOTE_COMPLETED
    private int countDown;
    private HashMap<String, Object> votes;

    // GSON 라이브러리 사용을 위해 빈 생성자가 필요함
    public Message() {
    }

    public Message(MessageType type, String sender, String receiver, String content, String password, String nickname) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.password = password;
        this.nickname = nickname;
        this.timestamp = Instant.now().toString();
    }

    public Message setType(MessageType type) {
        this.type = type;
        return this;
    }

    public Message setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public Message setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

//    public Message setJson(String json) {
//        this.json = json;
//        return this;
//    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }


    public Message setPassword(String password) {
        this.password = password;
        return this;
    }

    public Message setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Message setNovelRoomId(Integer novelRoomId) {
        this.novelRoomId = novelRoomId;
        return this;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public Message setParticipantsCount(int participantsCount) {
        this.participantsCount = participantsCount;
        return this;
    }

    public Message setNovelRoom(Message novelRoom) {
        this.novelRoom = novelRoom;
        return this;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public static Message fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Message.class);
    }

    public NovelRoom toNovelRoom() {
        return new NovelRoom(
                this.novelRoomId,
                this.novelRoomTitle,
                this.novelRoomDescription,
                this.novelParticipantIds != null ? String.join(",", this.novelParticipantIds) : null,
                this.maxParticipants,
                this.novelRoomStatus,
                null,  // createdAt
                this.novelContent,
                this.novelHostUser,
                null,  // currentVoteId
                this.votingDuration,
                this.submissionDuration
        );
    }
}
