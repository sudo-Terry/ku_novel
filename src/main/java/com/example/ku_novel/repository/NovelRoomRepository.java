package com.example.ku_novel.repository;

import com.example.ku_novel.domain.NovelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelRoomRepository extends JpaRepository<NovelRoom, Long> {

    // 특정 제목의 소설방이 존재하는지 확인
    boolean existsByTitle(String title);

    // 특정 상태의 소설방 조회
    List<NovelRoom> findByStatus(String status);

    // 방장이 생성한 모든 소설방 조회
    List<NovelRoom> findByHostUserId(String hostUserId);

    // 최대 참여 인원이 특정 값 이상인 소설방 조회
    List<NovelRoom> findByMaxParticipantsGreaterThanEqual(Integer minParticipants);
}