package com.example.ku_novel.client.connection;

import com.example.ku_novel.client.model.ClientDataModel;
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

    public void requestCreateNovelRoom(String roomTitle, String roomDescription, int submissionDuration, int votingDuration, int novelistCount) {
        Message createNovelRoomMessage = new Message();
        createNovelRoomMessage.setType(MessageType.ROOM_CREATE);
        createNovelRoomMessage.setSender(ClientDataModel.getInstance().getUserId());
        createNovelRoomMessage.setNovelRoomTitle(roomTitle);
        createNovelRoomMessage.setNovelRoomDescription(roomDescription);
        createNovelRoomMessage.setMaxParticipants(novelistCount);
        createNovelRoomMessage.setSubmissionDuration(submissionDuration);
        createNovelRoomMessage.setVotingDuration(votingDuration);

        writer.println(createNovelRoomMessage.toJson());
    }

    public void requestRoomFetchByTitle(String roomTitle) {
        Message fetchByTitleMessage = new Message();
        fetchByTitleMessage.setType(MessageType.ROOM_FETCH_BY_TITLE);
        fetchByTitleMessage.setNovelRoomTitle(roomTitle);

        writer.println(fetchByTitleMessage.toJson());
    }

    public void requestRoomJoin(int roomId) {
        Message joinRoomMessage = new Message();
        joinRoomMessage.setType(MessageType.ROOM_JOIN);
        joinRoomMessage.setSender(ClientDataModel.getInstance().getUserId());
        joinRoomMessage.setNovelRoomId(roomId);

        writer.println(joinRoomMessage.toJson());
    }

    public void requestRoomFetchActive(){
        Message roomListMessage = new Message();
        roomListMessage.setType(MessageType.ROOM_FETCH_ACTIVE);

        writer.println(roomListMessage.toJson());
    }

    public void requestAuthorApply(String senderId, int novelRoomId) {
        Message authorApplyMessage = new Message();
        authorApplyMessage.setType(MessageType.AUTHOR_APPLY);
        authorApplyMessage.setSender(senderId);
        authorApplyMessage.setNovelRoomId(novelRoomId);

        writer.println(authorApplyMessage.toJson());
    }

    public void requestAuthorWrite(String senderId, int novelRoomId, String content) {
        Message authorWriteMessage = new Message();
        authorWriteMessage.setType(MessageType.AUTHOR_WRITE);
        authorWriteMessage.setSender(senderId);
        authorWriteMessage.setNovelRoomId(novelRoomId);
        authorWriteMessage.setContent(content);

        writer.println(authorWriteMessage.toJson());
    }

    public void requestVote(String senderId, int novelRoomId, int voteChoice) {
        Message voteMessage = new Message();
        voteMessage.setType(MessageType.VOTE);
        voteMessage.setSender(senderId);
        voteMessage.setNovelRoomId(novelRoomId);
        voteMessage.setVoteChoice(voteChoice);

        writer.println(voteMessage.toJson());
    }

    public void requestRefreshHome(String senderId) {
        Message refreshHomeMessage = new Message();
        refreshHomeMessage.setType(MessageType.REFRESH_HOME);
        refreshHomeMessage.setSender(senderId);

        writer.println(refreshHomeMessage.toJson());
    }

    public void requestNovelRoomMessageSend(String SenderId, int novelRoomId, String content) {
        Message novelRoomChatMessage = new Message();
        novelRoomChatMessage.setType(MessageType.MESSAGE_SEND);
        novelRoomChatMessage.setSender(SenderId);
        novelRoomChatMessage.setNovelRoomId(novelRoomId);
        novelRoomChatMessage.setContent(content);

        writer.println(novelRoomChatMessage.toJson());
    }

    public void requestRoomStatusUpdate(int maxParticipant, String novelRoomTitle, String novelRoomDescription, boolean isNovelEnded) {
        Message roomStatusUpdateMessage = new Message();
        roomStatusUpdateMessage.setType(MessageType.ROOM_STATUS_UPDATE);
        roomStatusUpdateMessage.setNovelRoomId(ClientDataModel.getInstance().getCurrentRoomId());
        roomStatusUpdateMessage.setSender(ClientDataModel.getInstance().getUserId());
        roomStatusUpdateMessage.setNovelRoomTitle(novelRoomTitle);
        roomStatusUpdateMessage.setNovelRoomDescription(novelRoomDescription);
        roomStatusUpdateMessage.setMaxParticipants(maxParticipant);
        roomStatusUpdateMessage.setNovelEnded(isNovelEnded ? "Y" : "N");

        writer.println(roomStatusUpdateMessage.toJson());
    }

    public void requestAttendanceCheck(String senderId) {
        Message attendanceCheckMessage = new Message();
        attendanceCheckMessage.setType(MessageType.ATTENDANCE_CHECK);
        attendanceCheckMessage.setSender(senderId);

        writer.println(attendanceCheckMessage.toJson());
    }

    public void requestAuthorRejected() {
        Message authorRejectedMessage = new Message();
        // authorRejectedMessage.setType(MessageType.AUTHOR_REJECTED);
        authorRejectedMessage.setSender(ClientDataModel.getInstance().getUserId());
        authorRejectedMessage.setNovelRoomId(ClientDataModel.getInstance().getCurrentRoomId());

        writer.println(authorRejectedMessage.toJson());
    }

    public void requestAuthorApproved() {
        Message authorApprovedMessage = new Message();
        // authorApprovedMessage.setType(MessageType.AUTHOR_APPROVED);
        authorApprovedMessage.setSender(ClientDataModel.getInstance().getUserId());
        authorApprovedMessage.setNovelRoomId(ClientDataModel.getInstance().getCurrentRoomId());

        writer.println(authorApprovedMessage.toJson());
    }
}
