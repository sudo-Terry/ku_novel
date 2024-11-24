package com.example.ku_novel.service;


import com.example.ku_novel.domain.NovelRoom;
import com.example.ku_novel.repository.NovelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class NovelRoomService {

    private final NovelRoomRepository novelRoomRepository;

    @Autowired
    public NovelRoomService(NovelRoomRepository novelRoomRepository) {
        this.novelRoomRepository = novelRoomRepository;
    }

    // 소설 방 생성
    public NovelRoom createNovelRoom(String title, String description, String hostUserId, Integer maxParticipants, Integer submissionDuration, Integer votingDuration) {
        NovelRoom novelRoom = NovelRoom.builder()
                .title(title)
                .description(description)
                .participantIds(null)
                .maxParticipants(maxParticipants)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .novelContent(null)
                .hostUserId(hostUserId)
                .submissionDuration(submissionDuration)
                .votingDuration(votingDuration)
                .currentVoteId(null)
                .build();

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
    public List<NovelRoom> getAllNovelRooms() {
        return novelRoomRepository.findAll();
    }

    // 소설 방 참가
    public void joinNovelRoom(Long id, String participantId) {
        Optional<NovelRoom> optionalRoom = novelRoomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            NovelRoom novelRoom = optionalRoom.get();
            // 참가자 목록에 추가
            String currentParticipants = novelRoom.getParticipantIds();
            String updatedParticipants = (currentParticipants == null || currentParticipants.isEmpty())
                    ? participantId
                    : currentParticipants + "," + participantId;

            // 최대 참여 인원 초과 확인
            if (updatedParticipants.split(",").length > novelRoom.getMaxParticipants()) {
                throw new IllegalStateException("참가 인원이 초과되었습니다.");
            }

            novelRoom.setParticipantIds(updatedParticipants);
            novelRoomRepository.save(novelRoom);
        } else {
            throw new IllegalArgumentException("해당 ID의 소설 방을 찾을 수 없습니다.");
        }
    }
}
