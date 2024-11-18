package com.example.ku_novel.service;

import com.example.ku_novel.domain.NovelRoom;
import com.example.ku_novel.repository.NovelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NovelRoomService {

    private final NovelRoomRepository novelRoomRepository;

    @Autowired
    public NovelRoomService(NovelRoomRepository novelRoomRepository) {
        this.novelRoomRepository = novelRoomRepository;
    }

    // 소설 방 생성 (increment 하게 증가하도록 수정해야함.)
    public NovelRoom createNovelRoom(String title, String description, String hostUserId, Integer maxParticipants) {
        NovelRoom novelRoom = new NovelRoom(1, title, description, hostUserId, maxParticipants, "ACTIVE", java.time.LocalDateTime.now(), null, hostUserId, null);
        return novelRoomRepository.save(novelRoom);
    }

    // 아이디로 소설 방 조회
    public Optional<NovelRoom> getNovelRoomById(Long id) {
        return novelRoomRepository.findById(id);
    }

    // 활성화된 소설 방 조회
    public List<NovelRoom> getActiveNovelRooms() {
        return novelRoomRepository.findByStatus("ACTIVE");
    }

    // 제목으로 소설 방 조회
    public List<NovelRoom> getNovelRoomByTitle(String title) {
        return novelRoomRepository.findByTitleContaining(title);
    }

    // 모든 소설 방 조회

    // 소설 방 참가

    // 소설 방 상태 업데이트

    // 소설 내용 업데이트
}
