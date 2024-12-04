package com.example.ku_novel.service;


import com.example.ku_novel.domain.User;
import com.example.ku_novel.domain.Vote;
import com.example.ku_novel.repository.VoteRepository;
import com.example.ku_novel.utils.VoteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote getVoteById(int voteId) {
        return voteRepository.findById(voteId).orElseThrow(() -> new IllegalArgumentException("Vote not found: " + voteId));
    }

    public void updateVoteStatus(int voteId, String status) {
        Optional<Vote> voteOptional = voteRepository.findById(voteId);
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            vote.setStatus(status);
            voteRepository.save(vote);
        } else {
            throw new IllegalArgumentException("Vote not found: " + voteId);
        }
    }

    public void updateCreatedAtToNow(int voteId) {
        Optional<Vote> voteOptional = voteRepository.findById(voteId);
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            vote.setCreatedAt(LocalDateTime.now()); // 현재 시간으로 설정
            voteRepository.save(vote); // 업데이트된 정보 저장
        } else {
            throw new IllegalArgumentException("Vote ID not found: " + voteId);
        }
    }

    public void initializeVote(int voteId) {
        Optional<Vote> voteOptional = voteRepository.findById(voteId);
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            vote.setStatus("VOTE_COMPLETED");
            vote.setContentOptions(null);
            vote.setCreatedAt(null);
            vote.setVotes(null);
            voteRepository.save(vote); // 업데이트된 정보 저장
        } else {
            throw new IllegalArgumentException("Vote ID not found: " + voteId);
        }
    }

    public void addContentOption(int voteId, String newContent) {
        Optional<Vote> voteOptional = voteRepository.findById(voteId);
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();

            // JSON 문자열에서 List<String>으로 변환
            List<String> contentOptions = vote.getContentOptions();

            // 새로운 항목 추가
            contentOptions.add(newContent);

            // List<String>을 JSON 문자열로 변환
            String updatedContentOptionsJson = VoteUtils.toContentOptions(contentOptions);

            // 업데이트된 JSON 문자열 저장
            vote.setContentOptions(updatedContentOptionsJson);
            voteRepository.save(vote);
        } else {
            throw new IllegalArgumentException("Vote ID not found: " + voteId);
        }
    }

    public void addVotes(int voteId, String userId, String votedContent) {
        Optional<Vote> voteOptional = voteRepository.findById(voteId);
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();

            if (!vote.getStatus().equals("VOTING_ENABLED")) {
                throw new IllegalArgumentException("투표 가능한 시간이 아닙니다.");
            }

            HashMap<String, Object> votes = vote.getVotes();
            votes.put(userId, votedContent);

            // map을 JSON 문자열로 변환
            String json = VoteUtils.toJsonFromMap(votes);

            // 업데이트된 JSON 문자열 저장
            vote.setVotes(json);
            voteRepository.save(vote);
        } else {
            throw new IllegalArgumentException("Vote ID not found: " + voteId);
        }
    }

//    public String getVoteStatus(int id, int authorWriteMinutes, int votingMinutes) {
//        Vote vote = voteRepository.findById(id).orElse(null);
//
//        // vote가 없는 경우
//        if (vote == null) {
//            throw new IllegalArgumentException("Vote not found.");
//        }
//
//        LocalDateTime createdAt = vote.getCreatedAt();
//        LocalDateTime now = LocalDateTime.now();
//
//        if (createdAt == null) {
//            // created_at이 null인 경우 현재 시간을 설정
//            vote.setCreatedAt(now);
//            voteRepository.save(vote);
//            return "WRITER_ENABLED"; // 소설가 작성 가능
//        }
//
//        // 소설가 작성 종료 시간 계산
//        LocalDateTime authorWriteEndTime = createdAt.plusMinutes(authorWriteMinutes);
//        // 투표 종료 시간 계산
//        LocalDateTime votingEndTime = authorWriteEndTime.plusMinutes(votingMinutes);
//
//        if (now.isBefore(authorWriteEndTime)) {
//            // 소설가 작성 가능 시간 이내
//            return "WRITER_ENABLED";
//        } else if (now.isBefore(votingEndTime)) {
//            // 투표 가능 시간 이내
//            return "VOTING_ENABLED";
//        } else {
//            // 투표가 완료된 상태: 가장 많은 득표수 계산 및 소설방 업데이트 로직 추가
//
//            // 투표 필드 초기화
//            vote.setVotes(null);
//            vote.setContentOptions(null);
//            vote.setCreatedAt(null);
//            voteRepository.save(vote);
//            return "VOTE_COMPLETED";
//        }
//    }

    // 투표 생성과 관련된 메소드는 NovelRoomService에서 구현해서 투표 관련 상호작용 로직만 구현.

    //   클라이언트가 “소설 작성 버튼”, “투표 진행”, “투표 안건 등록”과 같은 투표관련 요청을 보내면 서버는 항상 아래 프로세스를 거침. (서버가 비동기적으로 알아서 투표 상태를 처리하는게 아니라 투표 관련 클라이언트의 요청을 받으면 이전의 데이터를 확인하여 투표를 관리하고 최신화함):
    //
    //    1. created_at 를 확인함. (알파벳 순으로 항상 확인해야함)
    //    1. created_at 이 null 이면, 현재시간으로 세팅, 소설가 작성 가능 시간(소설 등록 요청 수락 가능)
    //    2. created_at 이 “(방장이 설정한) 소설가 작성시간” 이내의 시간일 경우, 소설가 작성 가능 시간(소설 등록 요청 수락 가능)
    //    3. created_at 이 “(방장이 설정한) 소설가 작성시간 + 고정 투표 시간” 이내의 시간일 경우, 투표 가능 시간(투표 요청 수락 가능)
    //    4. 그 외인 경우, 이미 투표가 완료된 상태이므로 votes에서 가장 많은 득표수 계산해서 소설방의 novel_content 업데이트하고 투표필드의 모든 값을 null 로 초기화 ⇒ 0초가 되면 클라이언트가 한번 요청해줘야 결과 받을수있음
}
