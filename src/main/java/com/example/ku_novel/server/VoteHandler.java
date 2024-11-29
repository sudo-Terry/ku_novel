package com.example.ku_novel.server;

import com.example.ku_novel.domain.Vote;
import com.example.ku_novel.service.VoteService;

public class VoteHandler extends Thread {
    private final int voteId;
    private final int authorWriteMinutes;
    private final int votingMinutes;
    private final VoteService voteService;

    public VoteHandler(int voteId, int authorWriteMinutes, int votingMinutes, VoteService voteService) {
        this.voteId = voteId;
        this.authorWriteMinutes = authorWriteMinutes;
        this.votingMinutes = votingMinutes;
        this.voteService = voteService;
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

            // 상태를 VOTE_COMPLETED 업데이트하고 최종결과 저장하고 모든 클라이언트에게 broadcast
            voteService.finalizeVoteResults(voteId);
            System.out.println("Vote ID " + voteId + " VOTE_COMPLETED");

        } catch (InterruptedException e) {
            System.err.println("VoteHandler Error" + voteId);
            Thread.currentThread().interrupt();
        }
    }
}

