package com.example.ku_novel.server;

import com.example.ku_novel.domain.Vote;
import com.example.ku_novel.service.NovelRoomService;
import com.example.ku_novel.service.VoteService;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class VoteHandler extends Thread {
    private final int voteId;
    private final int authorWriteMinutes;
    private final int votingMinutes;
    private final VoteService voteService;
    private final NovelRoomService novelRoomService;

    public VoteHandler(int voteId, int authorWriteMinutes, int votingMinutes, VoteService voteService, NovelRoomService novelRoomService) {
        this.voteId = voteId;
        this.authorWriteMinutes = authorWriteMinutes;
        this.votingMinutes = votingMinutes;
        this.voteService = voteService;
        this.novelRoomService = novelRoomService;
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

            // 상태를 VOTING_ENABLED로 바꿈.
            voteService.updateVoteStatus(voteId, "VOTING_ENABLED");
            System.out.println("Vote ID " + voteId + " VOTING_ENABLED");

            // 투표 종료까지 대기
            Thread.sleep(votingMinutes * 60 * 1000L);

            // 상태를 VOTE_COMPLETED 업데이트하고 최종결과 저장하고
            finalizeVoteAndUpdateNovel();
            System.out.println("Vote ID " + voteId + " VOTE_COMPLETED");

            // Todo: 모든 클라이언트에게 broadcast

        } catch (InterruptedException e) {
            System.err.println("VoteHandler Error" + voteId);
            Thread.currentThread().interrupt();
        }
    }


    private void finalizeVoteAndUpdateNovel() {
        // Vote 가져오기
        Vote vote = voteService.getVoteById(voteId);

        // votes JSON을 Map으로 변환
        HashMap<String, Object> votes = vote.getVotes();

        if(votes != null) {
            // 득표수 계산
            HashMap<String, Integer> voteCounts = new HashMap<>();
            for (Object value : votes.values()) {
                String option = value.toString();
                voteCounts.put(option, voteCounts.getOrDefault(option, 0) + 1);
            }

            // 가장 많은 득표수를 받은 항목 찾기 (공동 득표일 경우 랜덤 선택)
            String topVotedContent = getTopVotedContent(voteCounts);

            // Novel의 내용을 업데이트
            novelRoomService.appendContentToNovel(vote.getNovelRoomId(), topVotedContent);
        }

        // vote 초기화
        voteService.initializeVote(voteId);
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
}

