package com.example.ku_novel.service;


import com.example.ku_novel.domain.Vote;
import com.example.ku_novel.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    // 투표 생성
    public Vote createVote(int novelRoomId) {
        Vote vote = Vote.builder()
                .novelRoomId(novelRoomId)
                .build();
        return voteRepository.save(vote);
    }
}
