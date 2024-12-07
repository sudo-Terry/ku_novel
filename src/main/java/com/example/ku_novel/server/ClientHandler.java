package com.example.ku_novel.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

import com.example.ku_novel.common.*;
import com.example.ku_novel.domain.*;
import com.example.ku_novel.service.NovelRoomService;
import com.example.ku_novel.service.UserService;
import com.example.ku_novel.service.VoteService;
import com.example.ku_novel.utils.ParticipantUtils;
import com.google.gson.Gson;

class ClientHandler implements Runnable {
    private static final HashMap<String, PrintWriter> activeClients = new HashMap<>();
    private static final Map<Integer, Set<String>> roomUsers = new HashMap<>(); // 소설방별 접속중인 유저 아이디 관리
    private static final Map<Integer, Set<String>> roomApplicants = new HashMap<>(); // 방별 소설가 신청자
    private static final Map<Integer, Set<String>> roomSubmittedAuthors = new HashMap<>();


    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String id;

    private final UserService userService;
    private final NovelRoomService novelRoomService;
    private final VoteService voteService;

    public ClientHandler(Socket clientSocket, UserService userService, NovelRoomService novelRoomService, VoteService voteService) {
        this.socket = clientSocket;
        this.userService = userService;
        this.novelRoomService = novelRoomService;
        this.voteService = voteService;
        try {
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String receivedMessage;
            while ((receivedMessage = in.readLine()) != null) {
                processClientRequest(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private Message parseMessage(String messageJson) {
        Gson gson = new Gson();
        return gson.fromJson(messageJson, Message.class);
    }

    private void processClientRequest(String messageJson) {
        // JSON 파싱하여 MessageType 분기 처리
        // MessageType에 따라 비즈니스 로직 호출

        Message message = parseMessage(messageJson);
        System.out.println("[RECEIVE] " + message);

        switch (message.getType()) {
            case LOGIN:
                handleLogin(message);
                break;
            case LOGOUT:
                handleLogout(messageJson);
                break;
            case SIGNUP:
                handleSignUp(message);
                break;
            case ID_CHECK:
                checkId(message);
                break;
            case NICKNAME_CHECK:
                checkNickname(message);
                break;
            case REFRESH_HOME:
                handleRefreshHome(message);
                break;
            case ROOM_CREATE:
                handleCreateRoom(message);
                break;
//            case ROOM_FETCH_BY_ID:
//                handleGetRoomById(message);
//                break;
            case ROOM_FETCH_BY_TITLE:
                handleGetRoomByTitle(message);
                break;
            case ROOM_FETCH_ACTIVE:
                handleGetActiveRooms();
                break;
            case ROOM_FETCH_ALL:
                handleGetAllRooms();
                break;
            case ATTENDANCE_CHECK:
                handleAttendance(message);
                break;
            case ROOM_FETCH_BY_COMPLETED:
                handleCompletedRoom();
                break;
            case ROOM_JOIN:
                handleJoinRoom(message);
                break;
            case ROOM_LEAVE:
                handleLeaveRoom(message);
                break;
            case MESSAGE_SEND:
                handleChatMessage(message);
                break;
            case AUTHOR_APPLY:
                handleApplyAuthor(message);
                break;
            case AUTHOR_WRITE:
                handleWriteNovel(message);
                break;
            case AUTHOR_APPROVE:
                handleApproveAuthor(message);
                break;
            case AUTHOR_REJECTED:
                handleRejectAuthor(message);
                break;
            case VOTE_FETCH_BY_ID:
                handleVoteFetch(message);
                break;
            case VOTE:
                handleVote(message);
                break;
            case ROOM_STATUS_UPDATE:
                handleUpdateRoomSetting(message);
                break;
            case ROOM_FETCH_RANK:
                handleRankingNovelRooms();
                break;
//            case ROOM_CLOSE:
//                handleCloseNovelRoom(message);
//                break;
            // case CHAT:
            // handleChatMessage(messageJson);
            // break;
            // 기타 요청 처리...
        }

        // handleVoteSummision test
//        message.setNovelVoteId(1);
//        message.setSender("aa");
//        message.setContent("추가한 내용");
//        handleVote(message);


        // vote 테스트
        //        message.setNovelVoteId(5);
        //        message.setNovelRoomId(5);
        //        handleVote(message);
        //
        //
        // vote fecth 테스트
//                message.setNovelVoteId(1);
//                message.setNovelRoomId(1);
//                handleVoteFetch(message);
    }


    private void handleVote(Message message) {
        String sender = message.getSender();
        String content = message.getContent();
        Message response = new Message().setType(MessageType.VOTE_FAILED);

        try {
            // ToDo 유효한 사용자아이디인지(굳이?), 투표 가능한 상태인지 확인, 투표한 아이템이 유효한지 확인해야함.

            int voteId = message.getNovelVoteId();
            Vote vote = voteService.getVoteById(voteId);

            // 이미 투표했는지 검사
            if (vote.getVotes().containsKey(sender)) {
                response.setContent("이미 투표를 했습니다. (1인 1투표)");
                sendMessageToCurrentClient(response);
                return;
            }

            voteService.addVotes(voteId, sender, content);

            NovelRoom room = novelRoomService.getNovelRoomByCurrentVoteId(voteId);
            int roomId = room.getId();

            // 모든 사용자에게 알림 전송
            synchronized (roomUsers) {
                Set<String> usersInRoom = roomUsers.get(roomId);
                if (usersInRoom != null) {
                    Message voteMessage = voteService.getVoteById(voteId).toMessage();
                    for (String userId : usersInRoom) {
                        response
                                .setType(MessageType.VOTE_SUCCESS)
                                .setContent("투표 갱신 완료");
                        response.setVote(voteMessage);
//                        response.setSender(sender);
                        sendMessageToUser(userId, response);
                    }
                }
            }
        } catch (Exception e) {
            response
                    .setType(MessageType.VOTE_FAILED)
                    .setContent("에러가 발생했습니다" + e.getMessage());
            sendMessageToUser(sender, response);
        }
    }


//    private void handleAuthorApply(Message message) {
//        Message responseMessage = new Message();
//        try {
//            String userId = message.getSender();
//            int novelRoomId = message.getNovelRoomId();
//            Optional<NovelRoom> room = novelRoomService.getNovelRoomById(novelRoomId);
//
//            if (room.isPresent()) {
//                String hostUserId = room.get().getHostUserId();
//
//                synchronized (roomUsers) {
//                    if (roomUsers.containsKey(novelRoomId)) {
//                        if (roomUsers.get(novelRoomId).contains(hostUserId)) {
//                            synchronized (activeClients) {
//                                if (activeClients.containsKey(hostUserId)) {
//                                    PrintWriter writer = activeClients.get(hostUserId);
//                                    responseMessage.setType(MessageType.AUTHOR_APPLY_RECEIVED).setSender(userId).setNovelRoomId(novelRoomId); // 방장에게 소설가 요청한 유저아이디와 방번호 보냄.
//                                    sendMessageToWriter(writer, responseMessage);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
//        // 클라이언트에게 따로 응답하지 않음.
//    }

    private void handleVoteFetch(Message message) {
        Message responseMessage = new Message();

        try {
            String userId = message.getSender();
            int voteId = message.getNovelVoteId();
            Vote vote = voteService.getVoteById(voteId);
//            System.out.println(vote.toMessage());

            switch (vote.getStatus()) {
                case "WRITER_ENABLED":
                    // todo
                    break;
                case "VOTING_ENABLED":
                    // todo
                    break;
                case "VOTE_COMPLETED":
                    // vote 시작 (synchronized 추후 고려)
                    VoteHandler voteHandler = new VoteHandler(voteId, vote.getSubmissionDuration(), vote.getVotingDuration(), voteService, novelRoomService, roomUsers, activeClients, roomSubmittedAuthors);
                    voteHandler.start();
                    vote = voteService.getVoteById(voteId); // vote 상태 업데이트
                    break;
                default:
                    break;
            }

            // 이전 소설 내용도 응답.
            Optional<NovelRoom> room = novelRoomService.getNovelRoomById(message.getNovelRoomId());
            room.ifPresent(novelRoom -> responseMessage.setNovelContent(novelRoom.getNovelContent()));

            responseMessage.setType(MessageType.VOTE_FETCH_BY_ID_SUCCESS).setVote(vote.toMessage());
        } catch (Exception e) {
            responseMessage.setType(MessageType.VOTE_FETCH_BY_ID_FAILED);
            responseMessage.setContent("오류가 발생하였습니다 : " + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

    private void handleSignUp(Message message) {
        String userId = message.getSender();
        String password = message.getPassword();
        String nickname = message.getNickname();

        boolean success = userService.registerUser(userId, password, nickname);
        Message responseMessage = new Message();

        if (success) {
            responseMessage.setType(MessageType.SIGNUP_SUCCESS)
                    .setContent("회원가입이 성공했습니다.");
        } else {
            responseMessage.setType(MessageType.SIGNUP_FAILED)
                    .setContent("아이디 또는 닉네임이 이미 존재합니다.");
        }
        sendMessageToCurrentClient(responseMessage);
    }

    private void checkId(Message message) {
        boolean isDuplicate = userService.isUserIdExists(message.getSender());
        Message responseMessage = new Message()
                .setType(isDuplicate ? MessageType.ID_INVALID : MessageType.ID_VALID)
                .setContent(isDuplicate ? "아이디가 이미 존재합니다." : "사용 가능한 아이디입니다.");
        sendMessageToCurrentClient(responseMessage);
    }

    private void checkNickname(Message message) {
        boolean isDuplicate = userService.isNicknameExists(message.getNickname());
        Message responseMessage = new Message()
                .setType(isDuplicate ? MessageType.NICKNAME_INVALID : MessageType.NICKNAME_VALID)
                .setContent(isDuplicate ? "닉네임이 이미 존재합니다." : "사용 가능한 닉네임입니다.");
        sendMessageToCurrentClient(responseMessage);
    }

    private void handleRefreshHome(Message message) {
        String userId = message.getSender();

        Message responseMessage = new Message();
        try {
            User user = userService.findById(userId);
            responseMessage.setPoint(user.getPoint());
            responseMessage.setNickname(user.getNickname());
            responseMessage.setType(MessageType.REFRESH_HOME_SUCCESS);
            responseMessage.setContent("새로고침 데이터 응답");
            responseMessage.setSender(user.getId());
            responseMessage.setPassword(user.getPassword());

            List<NovelRoom> activeNovelRooms = novelRoomService.getActiveNovelRooms();
            List<NovelRoom> participatingRooms = novelRoomService.getRoomsByParticipantId(user.getId());

            responseMessage.setActiveNovelRooms(_convertNovelRoomsToMessages(activeNovelRooms));
            responseMessage.setParticipatingNovelRooms(_convertNovelRoomsToMessages(participatingRooms));
        } catch (Exception e) {
            responseMessage.setType(MessageType.REFRESH_HOME_SUCCESS);
            responseMessage.setContent("오류가 발생하였습니다 : " + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

    private void handleLogin(Message message) {

        System.out.println("Login request received.");

        String id = message.getSender();
        String password = message.getPassword();

        // 로그인 비즈니스 로직 처리
        boolean isAuthenticated = userService.validateUserCredentials(id, password);

        Message responseMessage = new Message();
        if (isAuthenticated) {
            this.id = id;
            synchronized (activeClients) {
                activeClients.put(id, out);
            }

            User user = userService.findById(id);
            responseMessage.setPoint(user.getPoint());
            responseMessage.setNickname(user.getNickname());
            responseMessage.setType(MessageType.LOGIN_SUCCESS);
            responseMessage.setContent("로그인이 성공되었습니다.");
            responseMessage.setSender(id);
            responseMessage.setPassword(password);

            List<NovelRoom> activeNovelRooms = novelRoomService.getActiveNovelRooms();
            List<NovelRoom> participatingRooms = novelRoomService.getRoomsByParticipantId(user.getId());

            responseMessage.setActiveNovelRooms(_convertNovelRoomsToMessages(activeNovelRooms));
            responseMessage.setParticipatingNovelRooms(_convertNovelRoomsToMessages(participatingRooms));
        } else {
            responseMessage.setType(MessageType.LOGIN_FAILED);
            responseMessage.setContent("로그인 실패: 사용자 ID 또는 비밀번호가 잘못되었습니다.");
        }
        // 로그인 성공 시 클라이언트에 응답 전송
        sendMessageToCurrentClient(responseMessage);
    }

    private void handleLogout(String messageJson) {
        System.out.println("Logout request received.");
        // 유저가 속한 모든 소설방에서 제거
        synchronized (roomUsers) {
            roomUsers.values().forEach(users -> users.remove(id));
        }

        // 로그아웃 로직
        synchronized (activeClients) {
            activeClients.remove(id);
        }
        id = null;
    }

    /* 소설방 관련 로직 */

    // 소설방 생성 로직
    private void handleCreateRoom(Message message) {

        String title = message.getNovelRoomTitle();
        String description = message.getNovelRoomDescription();
        String hostUserId = message.getSender();
        Integer maxParticipants = message.getMaxParticipants();
        Integer submissionDuration = message.getSubmissionDuration();
        Integer votingDuration = message.getVotingDuration();

        Message responseMessage = new Message().setType(MessageType.ROOM_CREATE_FAILED).setContent("소설 방 생성에 실패했습니다.");

        if (userService.hasEnoughPoints(hostUserId)) {
            NovelRoom room = novelRoomService.createNovelRoom(title, description, hostUserId, maxParticipants, submissionDuration, votingDuration);

            if (room != null) {
                userService.deductPoints(hostUserId); // 방이 생성되어있으니까 500 차감
                responseMessage.setType(MessageType.ROOM_CREATE_SUCCESS)
                        .setContent("소설 방 생성에 성공했습니다.");
            }
        } else {
            responseMessage.setContent("포인트 부족으로 방을 생성할 수 없습니다.");
        }

        sendMessageToCurrentClient(responseMessage);

        // 자동 새로고침
        sendAllRefreshHome();
    }

    private void sendAllRefreshHome() {
        synchronized (activeClients) {
            for (String userId : activeClients.keySet()) {
                Message responseMessage = new Message();
                PrintWriter writer = activeClients.get(userId);

                try {
                    User user = userService.findById(userId);
                    responseMessage.setPoint(user.getPoint());
                    responseMessage.setNickname(user.getNickname());
                    responseMessage.setType(MessageType.REFRESH_HOME_SUCCESS);
                    responseMessage.setContent("자동 새로고침 데이터 응답");
                    responseMessage.setSender(user.getId());
                    responseMessage.setPassword(user.getPassword());

                    List<NovelRoom> activeNovelRooms = novelRoomService.getActiveNovelRooms();
                    List<NovelRoom> participatingRooms = novelRoomService.getRoomsByParticipantId(user.getId());

                    responseMessage.setActiveNovelRooms(_convertNovelRoomsToMessages(activeNovelRooms));
                    responseMessage.setParticipatingNovelRooms(_convertNovelRoomsToMessages(participatingRooms));
                    sendMessageToWriter(writer, responseMessage);
                } catch (Exception e) {
                    responseMessage.setType(MessageType.REFRESH_HOME_SUCCESS);
                    responseMessage.setContent("오류가 발생하였습니다 : " + e.getMessage());
                }
            }
        }
    }

//    // ID로 소설 방 조회
//    private void handleGetRoomById(Message message) {
//
//        Message responseMessage = new Message();
//        try {
//            NovelRoom room = novelRoomService.getNovelRoomById(message.getNovelRoomId())
//                    .orElseThrow(() -> new IllegalArgumentException("소설 방을 찾을 수 없습니다."));
//            JsonObject roomJson = new Gson().toJsonTree(room).getAsJsonObject();
//
//            responseMessage.setType(MessageType.ROOM_FETCH_BY_ID_SUCCESS);
//            responseMessage.setContent("소설 방 조회에 성공하였습니다.");
//            responseMessage.setNovelRoom(room.toMessage());
//            sendMessageToCurrentClient(responseMessage);
//        } catch (Exception e) {
//            responseMessage = new Message()
//                    .setType(MessageType.ROOM_FETCH_BY_ID_FAILED)
//                    .setContent("소설 방 조회에 실패했습니다: " + e.getMessage());
//            sendMessageToCurrentClient(responseMessage);
//        }
//    }

    // 제목으로 소설 방 조회
    private void handleGetRoomByTitle(Message message) {
        Message responseMessage = new Message().setType(MessageType.ROOM_FETCH_BY_TITLE_FAILED);
        try {
            List<NovelRoom> rooms = novelRoomService.getNovelRoomByTitle(message.getNovelRoomTitle());
            if (rooms.isEmpty()) {
                throw new IllegalArgumentException("해당 제목의 소설 방이 존재하지 않습니다.");
            }
            responseMessage
                    .setType(MessageType.ROOM_FETCH_BY_TITLE_SUCCESS)
                    .setContent("소설 방 조회 성공").setAllNovelRooms(_convertNovelRoomsToMessages(rooms));
        } catch (Exception e) {
            responseMessage.setContent("소설 방 조회에 실패했습니다: " + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

    // 활성화된 소설 방 조회
    private void handleGetActiveRooms() {
        Message responseMessage = new Message().setType(MessageType.ROOM_FETCH_ACTIVE_FAILED);
        try {
            List<NovelRoom> rooms = novelRoomService.getActiveNovelRooms();
            responseMessage
                    .setType(MessageType.ROOM_FETCH_ACTIVE_SUCCESS)
                    .setContent("소설 방 조회 성공했습니다.").setActiveNovelRooms(_convertNovelRoomsToMessages(rooms));
        } catch (Exception e) {
            responseMessage.setContent("소설 방 조회에 실패했습니다: " + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

    // 모든 소설 방 조회
    private void handleGetAllRooms() {
        Message responseMessage = new Message().setType(MessageType.ROOM_FETCH_ALL_FAILED);
        try {
            List<NovelRoom> allRooms = novelRoomService.getAllNovelRooms();
            responseMessage
                    .setType(MessageType.ROOM_FETCH_ALL_SUCCESS)
                    .setContent("소설 방 조회 성공했습니다.").setAllNovelRooms(_convertNovelRoomsToMessages(allRooms));
        } catch (Exception e) {
            responseMessage.setContent("소설 방 조회에 실패했습니다: " + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

    // 소설방 참가 로직
    private void handleJoinRoom(Message message) {
        Integer roomId = message.getNovelRoomId();
        String sender = message.getSender();

        // 기본 실패 응답 메시지
        Message responseMessage = new Message()
                .setType(MessageType.ROOM_JOIN_FAILED)
                .setContent("소설 방 참가에 실패했습니다.");

        try {
            // 소설방 정보 확인
            Optional<NovelRoom> novelRoomOpt = novelRoomService.getNovelRoomById(roomId);

            if (novelRoomOpt.isPresent()) {
                NovelRoom novelRoom = novelRoomOpt.get();

                // 소설방 사용자 관리
                synchronized (roomUsers) {
                    roomUsers.putIfAbsent(roomId, new HashSet<>());
                    roomUsers.get(roomId).add(sender);
                }

                // 현재 참여자 수 계산
                int participantCount = 0;
                synchronized (roomUsers) {
                    Set<String> set = roomUsers.get(roomId);
                    if (set != null) {
                        participantCount = set.size();
                    }
                }

                // 성공 응답 메시지 생성
                responseMessage = new Message()
                        .setType(MessageType.ROOM_JOIN_SUCCESS)
                        .setContent("소설 방 참가에 성공했습니다.")
                        .setParticipantsCount(participantCount) // 현재 참여자 수 설정
                        .setNovelRoom(novelRoom.toMessage()); // novelRoom 추가
                sendMessageToCurrentClient(responseMessage);

                sendRecentRoomInfo(roomId);

                System.out.println("User " + sender + " joined room " + roomId + ". Current participants: " + participantCount);
                return;
            } else {
                responseMessage.setContent("소설 방을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            responseMessage.setContent("소설 방 참가 처리 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }

        sendMessageToCurrentClient(responseMessage);
    }


    private void sendRecentRoomInfo(int roomId) {
        try {
            Optional<NovelRoom> novelRoomOpt = novelRoomService.getNovelRoomById(roomId);
            if (novelRoomOpt.isPresent()) {
                NovelRoom novelRoom = novelRoomOpt.get();

                // 현재 참여자 수 계산
                int participantCount = 0;
                synchronized (roomUsers) {
                    Set<String> set = roomUsers.get(roomId);
                    if (set != null) {
                        participantCount = set.size();
                        for (String userId : set) {
                            Message responseMessage = new Message()
                                    .setType(MessageType.ROOM_FETCH_PARTICIPANTS)
                                    .setContent("소설 방 정보 갱신")
                                    .setParticipantsCount(participantCount) // 현재 참여자 수 설정
                                    .setNovelRoom(novelRoom.toMessage());
                            sendMessageToUser(userId, responseMessage);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }


    /* 출석 로직 */
    private void handleAttendance(Message message) {
        Message responseMessage = new Message();

        try {
            String id = message.getSender();
            userService.attendanceCheck(id);

            responseMessage.setType(MessageType.ATTENDANCE_CHECK_SUCCESS)
                    .setContent("출석 체크 성공");
        } catch (Exception e) {
            responseMessage.setType(MessageType.ATTENDANCE_CHECK_FAILED)
                    .setContent("출석 체크 실패: " + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

    /* 완결 상태 소설방 목록 조회 로직 */

    private void handleCompletedRoom() {

        List<NovelRoom> deactivateRooms = novelRoomService.getDeactivateRoom();
        Message responseMessage = new Message();
        try {
            responseMessage.setType(MessageType.ROOM_FETCH_BY_COMPLETED_SUCCESS)
                    .setContent("완결 상태 소설 목록 조회 성공")
                    .setCompletedNovelRooms(_convertNovelRoomsToMessages(deactivateRooms));
        } catch (Exception e) {
            responseMessage.setType(MessageType.ROOM_FETCH_BY_COMPLETED_FAILED)
                    .setContent("완결 상태 소설 목록 조회 실패" + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

    /* 소설방 설정 변경 로직 */

    private void handleUpdateRoomSetting(Message message) {

        Message responseMessage = new Message();
        try {
            Integer roomId = message.getNovelRoomId();
            String title = message.getNovelRoomTitle();
            String novelRoomDescription = message.getNovelRoomDescription();
            String updatedStatus = message.getNovelEnded();

            // 소설방 설정 변경
            novelRoomService.updateNovelRoomSettings(roomId, title, novelRoomDescription, updatedStatus);

            synchronized (roomUsers) {
                Set<String> usersInRoom = roomUsers.get(roomId);
                if (usersInRoom != null) {
                    for (String userId : usersInRoom) {
                        Message updateMessage = new Message()
                                .setType(MessageType.ROOM_FETCH_BY_ID)
                                .setNovelRoomId(roomId)
                                .setContent("소설방 설정이 변경되었습니다.")
                                .setNovelRoom((novelRoomService.getNovelRoomById(roomId).orElseThrow().toMessage()))
                                .setNovelRoomStatus(updatedStatus);
                        sendMessageToUser(userId, updateMessage);
                    }
                }
            }

            // 성공 메시지
            responseMessage.setType(MessageType.ROOM_UPDATE_SETTING_SUCCESS)
                    .setNovelRoomId(roomId)
                    .setContent("소설방 설정이 성공적으로 변경되었습니다.");

            sendAllRefreshHome();
        } catch (Exception e) {
            // 실패 메시지
            responseMessage.setType(MessageType.ROOM_UPDATE_SETTING_FAILED)
                    .setContent("소설방 설정 변경 실패: " + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

    /* 랭킹 순 정렬 로직 */
    public List<Map<String, Object>> getTopRoomsByParticipants(int limit) {

        List<NovelRoom> allNovelRooms = novelRoomService.getActiveNovelRooms();

        // 소설방 접속자 수 기준으로 정렬
        return allNovelRooms.stream()
                .map(novelRoom -> {
                    int roomId = novelRoom.getId();
                    int participantCount = roomUsers.getOrDefault(roomId, Collections.emptySet()).size();

                    // Map 형태로 변환
                    Map<String, Object> roomData = new HashMap<>();
                    roomData.put("roomId", roomId);
                    roomData.put("title", novelRoom.getTitle());
                    roomData.put("description", novelRoom.getDescription());
                    roomData.put("participantCount", participantCount);
                    return roomData;
                })
                .sorted((room1, room2) -> Integer.compare((int) room2.get("participantCount"), (int) room1.get("participantCount"))) // 내림차순 정렬
                .limit(limit) // 상위 limit개만 선택
                .collect(Collectors.toList());
    }


    public void handleRankingNovelRooms() {
        try {
            // 상위 20개의 소설방 가져오기
            List<Map<String, Object>> topRooms = getTopRoomsByParticipants(20);

            // Map 데이터를 Message로 변환
            List<Message> rankedMessages = topRooms.stream().map(roomData -> {
                Message roomMessage = new Message();
                roomMessage.setNovelRoomId((Integer) roomData.get("roomId"));
                roomMessage.setNovelRoomTitle((String) roomData.get("title"));
                roomMessage.setNovelRoomDescription((String) roomData.get("description"));
                roomMessage.setContent("참여자 수: " + roomData.get("participantCount"));
                return roomMessage;
            }).collect(Collectors.toList());

            // 클라이언트에 전송할 응답 생성
            Message response = new Message()
                    .setType(MessageType.ROOM_FETCH_RANK_SUCCESS)
                    .setContent("참여자 수 기준으로 정렬된 소설방 목록입니다.")
                    .setRankNovelRooms(rankedMessages);

            sendMessageToCurrentClient(response);
        } catch (Exception e) {
            // 오류 처리
            Message errorResponse = new Message()
                    .setType(MessageType.ROOM_FETCH_RANK_FAILED)
                    .setContent("방 정렬 중 오류 발생: " + e.getMessage());
            sendMessageToCurrentClient(errorResponse);
        }
    }


    /* 소설 작성 로직 */

    // 소설가 신청 로직
    private void handleApplyAuthor(Message message) {
        Integer roomId = message.getNovelRoomId();
        String sender = message.getSender();
        User user = userService.findNicknameById(sender);
        String nickname = user.getNickname();

        synchronized (roomApplicants) {
            roomApplicants.putIfAbsent(roomId, new HashSet<>());
            roomApplicants.get(roomId).remove(nickname);
            roomApplicants.get(roomId).add(nickname);

            try {
                // 방장 ID 조회
                String hostId = novelRoomService.getHostUserId(roomId);

                synchronized (roomUsers) {
                    if (roomUsers.containsKey(roomId) && roomUsers.get(roomId).contains(hostId)) {
                        // 방장이 활성 상태인 경우 메시지 전송
                        Message responseToApplicant = new Message()
                                .setType(MessageType.AUTHOR_APPLY_SUCCESS)
                                .setContent("소설가 신청이 접수되었습니다.")
                                .setSender(sender)
                                .setNovelRoomId(roomId);
                        sendMessageToUser(sender, responseToApplicant);

                        // 방장에게 알림 전송
                        Message notifyHost = new Message()
                                .setType(MessageType.AUTHOR_APPLY_RECEIVED)
                                .setContent("새로운 소설가 신청이 접수되었습니다.")
                                .setNickname(nickname)
                                .setNovelRoomId(roomId);
                        sendMessageToUser(hostId, notifyHost);
                    } else {
                        // 방장이 비활성 상태인 경우 신청자에게 거절 메시지 전송
                        Message rejection = new Message()
                                .setType(MessageType.AUTHOR_APPLY_REJECTED)
                                .setContent("소설가 신청이 거절되었습니다. 방장이 비활성 상태입니다.")
                                .setSender(sender)
                                .setNovelRoomId(roomId);
                        sendMessageToUser(sender, rejection);
                    }
                }
            } catch (Exception e) {
                System.err.println("방장 정보를 찾을 수 없습니다: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    // 소설가 승인 로직
    private void handleApproveAuthor(Message message) {
        Integer roomId = message.getNovelRoomId();
        String nickname = message.getContent();

        User user = userService.findIdByNickname(nickname);
        String applyUsrId = user.getId();

        synchronized (roomApplicants) {
            if (!roomApplicants.containsKey(roomId) || !roomApplicants.get(roomId).contains(nickname)) {
                System.out.println("Applicant not found in roomApplicants for roomId: " + roomId);
                return; // 신청자가 없으면 무시
            }

            Optional<NovelRoom> novelRoomOpt = novelRoomService.getNovelRoomById(roomId);

            if (novelRoomOpt.isPresent()) {
                NovelRoom novelRoom = novelRoomOpt.get();

                // ParticipantUtils 사용
                List<String> participantIds = ParticipantUtils.parseParticipantIds(novelRoom.getParticipantIds());
                System.out.println("Current participant IDs: " + participantIds);

                if (participantIds.contains(nickname)) {
                    sendMessageToUser(nickname, new Message()
                            .setType(MessageType.AUTHOR_REJECTED)
                            .setNovelRoomId(roomId)
                            .setContent("이미 소설가로 승인되었습니다."));
                    roomApplicants.get(roomId).remove(nickname);
                    return;
                }

                // 최대 참여자 확인
                if (participantIds.size() >= novelRoom.getMaxParticipants()) {
                    sendMessageToUser(nickname, new Message()
                            .setType(MessageType.AUTHOR_REJECTED)
                            .setNovelRoomId(roomId)
                            .setContent("소설가 신청 가능 인원이 이미 모두 찼습니다."));
                    roomApplicants.get(roomId).remove(nickname);
                    return;
                }

                // 새 참여자 추가
                String updatedParticipantIdsJson = ParticipantUtils.addParticipant(novelRoom.getParticipantIds(), applyUsrId);
                novelRoom.setParticipantIds(updatedParticipantIdsJson);
                novelRoomService.save(novelRoom);

                // 승인 메시지 전송
                Message responseToApplicant = new Message()
                        .setType(MessageType.AUTHOR_APPROVED)
                        .setNovelRoomId(roomId)
                        .setNovelRoom(novelRoom.toMessage())
                        .setContent("소설가로 승인되었습니다.");
                sendMessageToUser(applyUsrId, responseToApplicant);

                List<String> updatedParticipantIds = ParticipantUtils.parseParticipantIds(updatedParticipantIdsJson);

                synchronized (roomUsers) {
                    Set<String> usersInRoom = roomUsers.get(roomId);
                    if (usersInRoom != null) {
                        for (String userId : usersInRoom) {
                            Message updateMessage = new Message()
                                    .setType(MessageType.AUTHOR_LIST_UPDATE)
                                    .setContent("소설가 상태가 갱신되었습니다.")
                                    .setNovelRoomId(roomId)
                                    .setNovelParticipantIds(updatedParticipantIds);
                            sendMessageToUser(userId, updateMessage);
                        }
                    }
                }
            } else {
                System.out.println("NovelRoom not found for roomId=" + roomId);
            }
            roomApplicants.get(roomId).remove(nickname);
        }
    }

    // 소설가 승인 거절 로직
    public void handleRejectAuthor(Message message) {
        Integer roomId = message.getNovelRoomId();
        String nickname = message.getContent();
        User user = userService.findIdByNickname(nickname);
        String applyUsrId = user.getId();

        sendMessageToUser(applyUsrId, new Message()
                .setType(MessageType.AUTHOR_REJECTED)
                .setNovelRoomId(roomId)
                .setContent("소설가 신청이 거절되었습니다."));
    }

    private void handleWriteNovel(Message message) {
        String sender = message.getSender();

        try {
            int roomId = message.getNovelRoomId();
            String content = message.getContent();

            // 소설방 정보 확인
            Optional<NovelRoom> novelRoomOpt = novelRoomService.getNovelRoomById(roomId);
            if (novelRoomOpt.isEmpty()) {
                Message response = new Message()
                        .setType(MessageType.ROOM_NOT_FOUND)
                        .setContent("소설 방을 찾을 수 없습니다.")
                        .setNovelRoomId(roomId);
                sendMessageToUser(sender, response);
                return;
            }

            NovelRoom novelRoom = novelRoomOpt.get();
            int voteId = novelRoom.getCurrentVoteId();

            String participantIdsJson = novelRoom.getParticipantIds();
            List<String> participantIds = ParticipantUtils.parseParticipantIds(participantIdsJson);

            if (participantIds == null || !participantIds.contains(sender)) {
                Message response = new Message()
                        .setType(MessageType.AUTHOR_WRITE_REJECTED)
                        .setContent("소설 작성 권한이 없습니다.")
                        .setNovelRoomId(roomId);
                sendMessageToUser(sender, response);
                return;
            }

            // 이미 소설을 제출한 경우
            synchronized (roomSubmittedAuthors) {
                roomSubmittedAuthors.putIfAbsent(roomId, new HashSet<>());
                if (roomSubmittedAuthors.get(roomId).contains(sender)) {
                    Message response = new Message()
                            .setType(MessageType.NOVEL_ALREADY_SUBMITTED)
                            .setContent("이미 소설을 제출했습니다. 다음 턴까지 기다려 주세요.")
                            .setNovelRoomId(roomId);
                    sendMessageToUser(sender, response);
                    return;
                }

                // 소설 제출자로 등록
                roomSubmittedAuthors.get(roomId).add(sender);
            }

            // DB에 저장
            voteService.addContentOption(voteId, content);

            // 모든 사용자에게 알림 전송
            synchronized (roomUsers) {
                Set<String> usersInRoom = roomUsers.get(roomId);
                if (usersInRoom != null) {
                    Message voteMessage = voteService.getVoteById(voteId).toMessage();
                    for (String userId : usersInRoom) {
                        Message response = new Message()
                                .setType(MessageType.NOVEL_SUBMITTED)
                                .setContent("새 소설이 제출되었습니다.");
                        response.setVote(voteMessage);
                        response.setSender(sender);
                        sendMessageToUser(userId, response);
                    }
                }
            }
        } catch (Exception e) {
            Message response = new Message()
                    .setContent(e.getMessage());
            sendMessageToUser(sender, response);
        }
    }


    // 소설방 나가기 로직
    private void handleLeaveRoom(Message message) {
        Integer roomId = message.getNovelRoomId();
        String sender = message.getSender();

        try {
            // 소설방에서 사용자 제거
            synchronized (roomUsers) {
                if (roomUsers.containsKey(roomId)) {
                    roomUsers.get(roomId).remove(sender);
                    // 소설방에 아무도 없으면 roomUsers에서 소설방 삭제
                    if (roomUsers.get(roomId).isEmpty()) {
                        roomUsers.remove(roomId);
                    }
                } else {
                    throw new IllegalArgumentException("소설 방에 참가하지 않았습니다.");
                }
            }

            sendRecentRoomInfo(roomId);
        } catch (Exception e) {
        }

        // 나가기를 다른 사용자에게 응답할 필요가 없으면 굳이 클라이언트 응답 필요 없음.
    }

    private void handleChatMessage(Message message) {
        try {
            // 채팅 메시지 처리 로직
            System.out.println("Chat message received.");

            int roomId = message.getNovelRoomId();
            String sender = message.getSender();

            User user = userService.findById(sender);
            String nickname = user.getNickname();
            String content = message.getContent();

            synchronized (roomUsers) {
                if (!roomUsers.containsKey(roomId)) return;

                Set<String> usersInRoom = roomUsers.get(roomId);
                synchronized (activeClients) {
                    for (String userId : usersInRoom) {
                        PrintWriter writer = activeClients.get(userId);
                        if (writer != null) {
                            Message responseMessage = new Message().setType(MessageType.MESSAGE_RECEIVE).setContent(content).setSender(sender);
                            responseMessage.setNovelRoomId(roomId);
                            responseMessage.setNickname(nickname);
                            sendMessageToWriter(writer, responseMessage);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

// 안씀
//    private void broadcastMessageToAll(Message message) {
//        synchronized (activeClients) {
//            Collection<PrintWriter> collection = activeClients.values();
//            Iterator<PrintWriter> iterator = collection.iterator();
//            while (iterator.hasNext()) {
//                PrintWriter writer = iterator.next();
//                sendMessageToWriter(writer, message);
//            }
//        }
//    }

    private List<Message> _convertNovelRoomsToMessages(List<NovelRoom> rooms) {
        return rooms.stream()
                .map(NovelRoom::toMessage)
                .collect(Collectors.toList());
    }

    private void sendMessageToWriter(PrintWriter writer, Message message) {
        writer.println(message.toJson());
        System.out.println("[SEND] " + message);
    }

    private void sendMessageToCurrentClient(Message message) {
        sendMessageToWriter(out, message);
    }

    private void sendMessageToUser(String userId, Message message) {
        synchronized (activeClients) {
            PrintWriter writer = activeClients.get(userId);
            if (writer != null) {
                writer.println(message.toJson()); // JSON 변환 후 메시지 전송
                System.out.println("[SEND] " + message);
            }
        }
    }

    private void closeConnection() {
        try {
            if (id != null) {
                synchronized (roomUsers) {
                    roomUsers.values().forEach(users -> users.remove(id));
                }
                synchronized (activeClients) {
                    activeClients.remove(id);
                }
            }
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

