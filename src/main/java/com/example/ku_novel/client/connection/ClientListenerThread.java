package com.example.ku_novel.client.connection;

import com.example.ku_novel.client.model.ClientDataModel;
import com.example.ku_novel.client.ui.HomeUI;
import com.example.ku_novel.client.ui.UIHandler;
import com.example.ku_novel.common.Message;
import com.example.ku_novel.domain.NovelRoom;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            UIHandler uiHandler = UIHandler.getInstance();
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line + " 응답 도착");

                JsonObject jsonObject = gson.fromJson(line, JsonObject.class);
                String messageType = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "";

                if (messageType.isEmpty()) {
                    throw new IllegalArgumentException("메시지 타입이 없습니다.");
                }

                handleMessageType(messageType, jsonObject, uiHandler);
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private void handleMessageType(String messageType, JsonObject jsonObject, UIHandler uiHandler) {
        switch (messageType) {
            case "LOGIN_SUCCESS" -> handleLoginSuccess(jsonObject, uiHandler);
            case "LOGIN_FAILED", "SIGNUP_FAILED" , "ROOM_JOIN_FAILED", "REFRESH_HOME_FAILED",
                 "ROOM_CREATE_FAILED", "AUTHOR_APPLY_FAILED", "ATTENDANCE_CHECK_FAILED",
                 "VOTE_FETCH_BY_ID_FAILED", "VOTE_FAILED", "ROOM_UPDATE_SETTING_FAILED",
                 "NOVEL_ALREADY_SUBMITTED", "ROOM_FETCH_RANK_FAILED", "ROOM_FETCH_BY_COMPLETED_FAILED",
                 "PASSWORD_CHANGE_FAILED", "NICKNAME_CHANGE_FAILED", "PROFILE_IMAGE_CHANGE_FAILED" -> uiHandler.showAlertModal(
                    null, "경고", jsonObject.get("content").getAsString(), JOptionPane.ERROR_MESSAGE);
            case "ID_INVALID", "ID_VALID", "NICKNAME_INVALID", "NICKNAME_VALID", "AUTHOR_APPLY_SUCCESS",
                 "ATTENDANCE_CHECK_SUCCESS", "FAVOURITE_ADD_SUCCESS", "FAVOURITE_ADD_FAILED",
                 "ROOM_FETCH_FAVOURITE_FAILED", "AUTHOR_REJECTED", "NOVEL_SUBMITTED", "VOTE_SUCCESS",
                 "ROOM_WRITE_END", "ROOM_UPDATE_SETTING_SUCCESS", "AUTHOR_LIST_UPDATE", "AUTHOR_APPLY_REJECTED",
                 "PASSWORD_CHANGE_SUCCESS", "NICKNAME_CHANGE_SUCCESS", "PROFILE_IMAGE_CHANGE_SUCCESS"-> uiHandler.showAlertModal(
                    null, "정보", jsonObject.get("content").getAsString(), JOptionPane.INFORMATION_MESSAGE);
            case "SIGNUP_SUCCESS" -> handleSignupSuccess(jsonObject, uiHandler);
            case "REFRESH_HOME_SUCCESS" -> handleRefreshHomeSuccess(jsonObject, uiHandler);
            case "ROOM_FETCH_BY_TITLE_SUCCESS" -> handleRoomFetchByTitleSuccess(jsonObject, uiHandler);
            case "ROOM_FETCH_BY_TITLE_FAILED" -> handleRoomFetchByTitleFailed(jsonObject, uiHandler);
            case "ROOM_JOIN_SUCCESS" -> handleRoomJoinSuccess(jsonObject, uiHandler);
            case "ROOM_CREATE_SUCCESS" -> handleRoomCreateSuccess(jsonObject, uiHandler);
            case "MESSAGE_RECEIVE" -> handleChatMessageReceive(jsonObject, uiHandler);
            case "AUTHOR_APPLY_RECEIVED" -> handleAuthorApplyReceiverd(jsonObject, uiHandler);
            case "VOTE_FETCH_BY_ID_SUCCESS" -> handleVoteFetchByIdSuccess(jsonObject, uiHandler);
            case "ROOM_FETCH_FAVOURITE_SUCCESS" -> handleFetchFavouriteSuccess(jsonObject, uiHandler);
            case "AUTHOR_APPROVED" -> handleAuthorApproved(jsonObject, uiHandler);
            case "ROOM_FETCH_PARTICIPANTS" -> handleRoomFetchParticipants(jsonObject, uiHandler);
            case "VOTE_RESULT" -> handleVoteResult(jsonObject, uiHandler);
            case "ROOM_FETCH_BY_ID" -> handleRoomFetchById(jsonObject, uiHandler);
            case "ROOM_FETCH_RANK_SUCCESS" -> handleRoomFetchRankSuccess(jsonObject, uiHandler);
            case "ROOM_FETCH_BY_COMPLETED_SUCCESS" -> handleRoomFetchByCompletedSuccess(jsonObject, uiHandler);
            default -> enqueueMessage(jsonObject);
        }
    }

    private void handleRoomFetchByCompletedSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        dataModel.setChatRoomsCompletedFromJson(jsonObject);

        uiHandler.showDownloadModal();
    }

    private void handleRoomFetchRankSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        dataModel.setChatRoomsByRankFromJson(jsonObject);

        uiHandler.showRankingModal();
    }

    private void handleRoomFetchById(JsonObject jsonObject, UIHandler uiHandler) {
        try {
            JsonObject novelRoomObject = jsonObject.getAsJsonObject("novelRoom");
            if (novelRoomObject == null) {
                throw new IllegalArgumentException("novelRoom 객체가 없습니다.");
            }
            ClientDataModel dataModel = ClientDataModel.getInstance();
            dataModel.setNovelRoomTitle(novelRoomObject.get("novelRoomTitle").getAsString());
            dataModel.setNovelRoomDescription(novelRoomObject.get("novelRoomDescription").getAsString());
            uiHandler.repaintNovelRoomModalUI();
        } catch (Exception e) {
            System.err.println("handleRoomFetchById 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleVoteResult(JsonObject jsonObject, UIHandler uiHandler) {
        String newNovelContent = jsonObject.get("content").getAsString();
        uiHandler.showAlertModal(
                null,
                "정보",
                newNovelContent + "(이)가 투표로 선정되었습니다. 소설가들은 다음 소설 작성을 시작해주세요!",
                JOptionPane.INFORMATION_MESSAGE
        );

        uiHandler.updateNovelRoomNovelContentArea(newNovelContent);
    }

    private void handleRoomFetchParticipants(JsonObject jsonObject, UIHandler uiHandler) {
        ClientDataModel.getInstance().setParticipantsCount(jsonObject.get("participantsCount").getAsInt());

        uiHandler.updateNovelRoomParticipantsCount();
    }

    private void handleAuthorApproved(JsonObject jsonObject, UIHandler uiHandler) {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        dataModel.setChatRoomFromJson(jsonObject);
        uiHandler.showAlertModal(
                null, "정보", jsonObject.get("content").getAsString(), JOptionPane.INFORMATION_MESSAGE);

        uiHandler.repaintNovelRoomModalUI();
    }

    private void handleFetchFavouriteSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        dataModel.setChatRoomsFavoriteFromJson(jsonObject);

        // TODO : 데이터를 통해 마이페이지 다시 그리는 메소드 구현
        // uiHandler.
    }

    private void handleVoteFetchByIdSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        ClientDataModel dataModel = ClientDataModel.getInstance();

        if (jsonObject.has("vote")) {
            JsonObject voteObject = jsonObject.getAsJsonObject("vote");
            if (voteObject != null && voteObject.has("contentOptions")) {
                List<String> options = dataModel.getGson().fromJson(
                        voteObject.getAsJsonArray("contentOptions"),
                        List.class
                );
                dataModel.setVoteOptions(options);
                if(voteObject.get("voteStatus").getAsString().equals("WRITER_ENABLED")){
                    dataModel.setCountDownWrite(voteObject.get("countDown").getAsInt());
                    dataModel.setCountDownVote(0);
                }
                if(voteObject.get("voteStatus").getAsString().equals("VOTING_ENABLED")){
                    dataModel.setCountDownWrite(0);
                    dataModel.setCountDownVote(voteObject.get("countDown").getAsInt());
                }
            } else {
                System.out.println("contentOptions가 JSON에 없습니다.");
                dataModel.setVoteOptions(new ArrayList<>());
            }
        } else {
            System.out.println("vote 객체가 JSON에 없습니다.");
            dataModel.setVoteOptions(new ArrayList<>());
        }

        uiHandler.repaintVoteModalUI();
    }

    private void handleRoomCreateSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        uiHandler.showAlertModal(
                null, "정보", jsonObject.get("content").getAsString(), JOptionPane.INFORMATION_MESSAGE);
        ClientSenderThread.getInstance().requestRefreshHome(ClientDataModel.getInstance().getUserId());
    }

    private void handleRefreshHomeSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        System.out.println("새로고침 실행");

        ClientDataModel dataModel = ClientDataModel.getInstance();

        dataModel.setUserId(jsonObject.get("sender").getAsString());
        dataModel.setPassword(jsonObject.get("password").getAsString());
        dataModel.setUserName(jsonObject.get("nickname").getAsString());
        dataModel.setUserPoint(jsonObject.get("point").getAsString());
        dataModel.setChatRoomsFromJson(jsonObject);

        uiHandler.repaintMainUI();
    }

    private void handleLoginSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        System.out.println("로그인 성공");

        ClientDataModel dataModel = ClientDataModel.getInstance();

        dataModel.setUserId(jsonObject.get("sender").getAsString());
        dataModel.setPassword(jsonObject.get("password").getAsString());
        dataModel.setUserName(jsonObject.get("nickname").getAsString());
        dataModel.setUserPoint(jsonObject.get("point").getAsString());
        dataModel.setChatRoomsFromJson(jsonObject);

        uiHandler.disposeLoginUI();
        uiHandler.repaintMainUI();
    }

    private void handleSignupSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        uiHandler.disposeSignUpModalUI();
        uiHandler.showAlertModal(null, "정보", jsonObject.get("content").getAsString(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleRoomFetchByTitleSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        ClientDataModel dataModel = ClientDataModel.getInstance();
        dataModel.setChatRoomsSearchResultFromJson(jsonObject);

        uiHandler.showRoomSearchResults();
    }

    private void handleRoomFetchByTitleFailed(JsonObject jsonObject, UIHandler uiHandler) {
        String errorMessage = jsonObject.get("content").getAsString();
        uiHandler.showAlertModal(null, "오류", "방 검색 실패: " + errorMessage, JOptionPane.ERROR_MESSAGE);
        System.out.println("검색 실패: " + errorMessage);
    }

    private void handleRoomJoinSuccess(JsonObject jsonObject, UIHandler uiHandler) {
        try {
            JsonObject novelRoomObject = jsonObject.getAsJsonObject("novelRoom");
            if (novelRoomObject == null) {
                throw new IllegalArgumentException("novelRoom 객체가 없습니다.");
            }
            ClientDataModel dataModel = ClientDataModel.getInstance();
            dataModel.setChatRoomFromJson(jsonObject);
            dataModel.setParticipantsCount(jsonObject.get("participantsCount").getAsInt());
            uiHandler.showNovelRoomModalUI(dataModel.getCurrentRoomId());
        } catch (Exception e) {
            System.err.println("handleRoomJoinSuccess 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleChatMessageReceive(JsonObject jsonObject, UIHandler uiHandler) {
        try {
            int roomId = jsonObject.get("novelRoomId").getAsInt();
            // String userId = jsonObject.get("sender").getAsString(); // 필요한가? 일단 보류
            String nickname = jsonObject.get("nickname").getAsString();
            String content = jsonObject.get("content").getAsString();

            String formattedChat = "[" + nickname + "] : " + content;
            uiHandler.updateNovelRoomChat(roomId, formattedChat);
            System.out.println("Chat message processed: " + formattedChat);
        } catch (Exception e) {
            System.err.println("handleChatMessage 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleAuthorApplyReceiverd(JsonObject jsonObject, UIHandler uiHandler) {
        String nickname = jsonObject.get("nickname").getAsString();

        uiHandler.showAuthorAcceptModalUI(nickname);
    }

    private void enqueueMessage(JsonObject jsonObject) {
        Message message = gson.fromJson(jsonObject, Message.class);
        messageQueue.add(message);
        System.out.println(message + " 가 큐에 추가됨");
    }

    private void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("소켓 닫기 오류: " + e.getMessage());
        }
    }
}