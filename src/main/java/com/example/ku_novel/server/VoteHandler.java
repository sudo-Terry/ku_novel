package com.example.ku_novel.server;

import com.example.ku_novel.common.Message;
import com.example.ku_novel.common.MessageType;
import com.example.ku_novel.domain.Vote;
import com.example.ku_novel.service.NovelRoomService;
import com.example.ku_novel.service.VoteService;

import java.io.PrintWriter;
import java.util.*;

public class VoteHandler extends Thread {
    private final int voteId;
    private final int authorWriteMinutes;
    private final int votingMinutes;
    private final VoteService voteService;
    private final NovelRoomService novelRoomService;
    private final Map<Integer, Set<String>> roomUsers; // 소설방별 접속중인 유저 아이디 관리
    private final HashMap<String, PrintWriter> activeClients;
    private final Map<Integer, Set<String>> roomSubmittedAuthors;

    public VoteHandler(int voteId, int authorWriteMinutes, int votingMinutes, VoteService voteService, NovelRoomService novelRoomService, Map<Integer, Set<String>> roomUsers, HashMap<String, PrintWriter> activeClients, Map<Integer, Set<String>> roomSubmittedAuthors) {
        this.voteId = voteId;
        this.authorWriteMinutes = authorWriteMinutes;
        this.votingMinutes = votingMinutes;
        this.voteService = voteService;
        this.novelRoomService = novelRoomService;
        this.roomUsers = roomUsers;
        this.activeClients = activeClients;
        this.roomSubmittedAuthors = roomSubmittedAuthors;
        // 스레드 생성시 WRITER_ENABLED 로 변경
        voteService.updateVoteStatus(voteId, "WRITER_ENABLED");
        voteService.updateCreatedAtToNow(voteId);
    }

    @Override
    public void run() {
        try {
            System.out.println("Vote ID " + voteId + " 스레드 시작");

            // 소설가 작성 시간 끝날때까지 대기
            Thread.sleep(authorWriteMinutes * 60 * 1000L);
            int roomId = voteService.getVoteById(voteId).getNovelRoomId();
            endAuthorWritePhase(roomId);

            // 상태를 VOTING_ENABLED로 바꿈.
            voteService.updateVoteStatus(voteId, "VOTING_ENABLED");
            System.out.println("Vote ID " + voteId + " VOTING_ENABLED");

            // 투표 종료까지 대기
            Thread.sleep(votingMinutes * 60 * 1000L);

            // 상태를 VOTE_COMPLETED 업데이트하고 최종결과 저장하고 broadcast
            finalizeVoteAndUpdateNovel();
            System.out.println("Vote ID " + voteId + " VOTE_COMPLETED");

        } catch (InterruptedException e) {
            System.err.println("VoteHandler Error" + voteId);
            Thread.currentThread().interrupt();
        }


    }

    private void endAuthorWritePhase(int roomId) {
        // 소설 제출자 목록 초기화
        synchronized (roomSubmittedAuthors) {
            roomSubmittedAuthors.put(roomId, new HashSet<>());
            System.out.println("Room " + roomId + " submitted authors reset.");
        }

        Message broadcastMessage = new Message()
                .setType(MessageType.ROOM_WRITE_END)
                .setNovelRoomId(roomId)
                .setContent("소설 작성이 종료되었습니다. 모두 투표해주세요!");

        broadcastToRoom(roomId, broadcastMessage);
    }


    private void finalizeVoteAndUpdateNovel() {
        // Vote 가져오기
        Vote vote = voteService.getVoteById(voteId);
        Integer roomId = vote.getNovelRoomId();

        // votes JSON을 Map으로 변환
        HashMap<String, Object> votes = vote.getVotes();
        String topVotedContent = null;
        String novelContent = null;

        if (votes != null) {
            // 득표수 계산
            HashMap<String, Integer> voteCounts = new HashMap<>();
            for (Object value : votes.values()) {
                String option = value.toString();
                voteCounts.put(option, voteCounts.getOrDefault(option, 0) + 1);
            }

            // 가장 많은 득표수를 받은 항목 찾기 (공동 득표일 경우 랜덤 선택)
            topVotedContent = getTopVotedContent(voteCounts);

            // Novel의 내용을 업데이트
            novelContent = novelRoomService.appendContentToNovel(roomId, topVotedContent);
        }

        // vote 초기화
        voteService.initializeVote(voteId);

        if (topVotedContent != null) {
            // 모든 사용자에게 알림 전송
            synchronized (roomUsers) {
                Set<String> usersInRoom = roomUsers.get(roomId);
                if (usersInRoom != null) {
                    for (String userId : usersInRoom) {
                        Message response = new Message()
                                .setType(MessageType.VOTE_RESULT)
                                .setContent(topVotedContent);
                        response.setNovelVoteId(roomId);
                        response.setNovelContent(novelContent);
                        sendMessageToUser(userId, response);
                    }
                }
            }
        }
    }

    private String getTopVotedContent(HashMap<String, Integer> voteCounts) {
        // 득표수로 정렬
        int maxVotes = voteCounts.values().stream().mapToInt(v -> v).max().orElse(0);
        List<String> topOptions = voteCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotes)
                .map(HashMap.Entry::getKey)
                .toList();

        // 공동 득표 시 랜덤 선택
        if (!topOptions.isEmpty()) {
            Random random = new Random();
            return topOptions.get(random.nextInt(topOptions.size()));
        }

        return null; // 득표 항목이 없을 경우
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

    private void broadcastToRoom(int roomId, Message message) {
        synchronized (roomUsers) {
            Set<String> usersInRoom = roomUsers.get(roomId);
            if (usersInRoom != null) {
                for (String userId : usersInRoom) {
                    PrintWriter writer = activeClients.get(userId); // 사용자와 연결된 출력 스트림
                    if (writer != null) {
                        writer.println(message.toJson()); // 메시지를 JSON으로 변환 후 전송
                        System.out.println("[BROADCAST] Sent to " + userId + ": " + message);
                    } else {
                        System.err.println("[BROADCAST] No active client for userId: " + userId);
                    }
                }
            }
        }
    }

}

