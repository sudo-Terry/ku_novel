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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class ClientHandler implements Runnable {
    private static final HashMap<String, PrintWriter> activeClients = new HashMap<>();
    private static final Map<Integer, Set<String>> roomUsers = new HashMap<>(); // 소설방별 접속중인 유저 아이디 관리

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
            case ROOM_FETCH_BY_ID:
                handleGetRoomById(message);
                break;
            case ROOM_FETCH_BY_TITLE:
                handleGetRoomByTitle(message);
                break;
            case ROOM_FETCH_ACTIVE:
                handleGetActiveRooms();
                break;
            case ROOM_FETCH_ALL:
                handleGetAllRooms();
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
            // case ROOM_STATUS_UPDATE:
            // handleUpdateRoomStatus(message);
            // break;
            // case CHAT:
            // handleChatMessage(messageJson);
            // break;
            // 기타 요청 처리...
        }
    }

    private void handleVote(Message message) {
        String userId = message.getSender();
        int voteId = message.getVoteId();
        Vote vote = voteService.getVoteById(voteId);

        switch (vote.getStatus()) {
            case "WRITER_ENABLED":
                // todo
                break;
            case "VOTING_ENABLED":
                // todo
                break;
            case "VOTE_COMPLETED":
                // vote 시작 (synchronized 추후 고려)
                VoteHandler voteHandler = new VoteHandler(voteId, vote.getSubmissionDuration(), vote.getVotingDuration(), voteService);
                voteHandler.start();
                break;
            default:
                break;
        }

        System.out.println(vote.toMessage());
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
        try{
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
        } catch (Exception e){
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
        // 로그아웃 로직
        synchronized (activeClients) {
            activeClients.remove(id);
        }

        // 유저가 속한 모든 소설방에서 제거
        synchronized (roomUsers) {
            roomUsers.values().forEach(users -> users.remove(id));
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
    }

    // ID로 소설 방 조회
    private void handleGetRoomById(Message message) {
        Message responseMessage = new Message().setType(MessageType.ROOM_FETCH_BY_ID_FAILED);
        try {
            NovelRoom room = novelRoomService.getNovelRoomById(message.getNovelRoomId())
                    .orElseThrow(() -> new IllegalArgumentException("소설 방을 찾을 수 없습니다."));
            responseMessage.setType(MessageType.ROOM_FETCH_BY_ID_SUCCESS).setNovelRoom(room.toMessage());
        } catch (Exception e) {
            responseMessage.setContent("소설 방 조회에 실패했습니다: " + e.getMessage());
        }
        sendMessageToCurrentClient(responseMessage);
    }

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
        Message responseMessage = new Message().setType(MessageType.ROOM_JOIN_FAILED);

        try {
            Optional<NovelRoom> novelRoom = novelRoomService.getNovelRoomById(roomId);
            novelRoom.ifPresent(room -> responseMessage.setType(MessageType.ROOM_JOIN_SUCCESS).setContent("소설 방 참가에 성공했습니다.").setNovelRoom(room.toMessage()));

            // 소설방 사용자 관리
            synchronized (roomUsers) {
                roomUsers.putIfAbsent(roomId, new HashSet<>());
                roomUsers.get(roomId).add(message.getSender());
            }

        } catch (Exception e) {
            responseMessage.setContent("소설 방 참가에 실패했습니다: " + e.getMessage());
        }

        sendMessageToCurrentClient(responseMessage);
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

    private void closeConnection() {
        try {
            if (id != null) {
                synchronized (activeClients) {
                    activeClients.remove(id);
                }
                synchronized (roomUsers) {
                    roomUsers.values().forEach(users -> users.remove(id));
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

