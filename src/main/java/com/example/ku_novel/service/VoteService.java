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


    // 투표 생성과 관련된 메소드는 NovelRoomService에서 구현해서 투표 관련 상호작용 로직만 구현.
}
