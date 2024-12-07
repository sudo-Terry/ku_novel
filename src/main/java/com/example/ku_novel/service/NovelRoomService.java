package com.example.ku_novel.service;

import com.example.ku_novel.domain.NovelRoom;
import com.example.ku_novel.domain.Vote;
import com.example.ku_novel.utils.ParticipantUtils;
import com.example.ku_novel.repository.NovelRoomRepository;
import com.example.ku_novel.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NovelRoomService {

    private final NovelRoomRepository novelRoomRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public NovelRoomService(NovelRoomRepository novelRoomRepository, VoteRepository voteRepository) {
        this.novelRoomRepository = novelRoomRepository;
        this.voteRepository = voteRepository;
    }

    // 소설 방 생성
    public NovelRoom createNovelRoom(String title, String description, String hostUserId, Integer maxParticipants,
                                     Integer submissionDuration, Integer votingDuration) {
        // 초기 participantIds JSON 생성 (방장만 포함)
        String initialParticipants = ParticipantUtils.toParticipantIdsJson(List.of(hostUserId));

        NovelRoom novelRoom = NovelRoom.builder()
                .title(title)
                .description(description)
                .participantIds(initialParticipants)
                .maxParticipants(maxParticipants)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .novelContent(null)
                .hostUserId(hostUserId)
                .submissionDuration(submissionDuration)
                .votingDuration(votingDuration)
                .currentVoteId(null)
                .build();

        NovelRoom newRoom = novelRoomRepository.save(novelRoom);

        // Room 먼저 저장하고 아이디 얻은 다음에 투표 저장
        Vote vote = Vote.builder()
                .novelRoomId(newRoom.getId())
                .votingDuration(votingDuration)
                .submissionDuration(submissionDuration)
                .build();
        voteRepository.save(vote);

        // Vote 저장하고 투표아이디 얻은 다음에 Room 투표 아이디 업데이트
        newRoom.setCurrentVoteId(vote.getId());
        novelRoomRepository.save(newRoom);

        return newRoom;
    }

    // 특정 participantId가 포함된 소설방 조회
    public List<NovelRoom> getRoomsByParticipantId(String participantId) {
        String escapedId = "%\"" + participantId + "\"%";
        return novelRoomRepository.findByParticipantId(escapedId);
    }

    public String appendContentToNovel(Integer novelRoomId, String newContent) {
        Optional<NovelRoom> novelRoom = novelRoomRepository.findById(novelRoomId);
        if (novelRoom.isPresent()) {
            NovelRoom room = novelRoom.get();
            String originalContent = room.getNovelContent();

            if (newContent == null)
                return originalContent;

            String novelContent = (originalContent == null ? newContent : (originalContent + "\n\n" + newContent));
            room.setNovelContent(novelContent);
            novelRoomRepository.save(room);

            return novelContent;
        }
        return null;
    }

    // 아이디로 소설 방 조회
    public Optional<NovelRoom> getNovelRoomById(Integer id) {
        return novelRoomRepository.findById(id);
    }

    // 투표 아이디로 소설 방 조회
    public NovelRoom getNovelRoomByCurrentVoteId(Integer voteId) {
        return novelRoomRepository.findByCurrentVoteId(voteId);
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

    // 완결 상태의 소설방 조회
    public List<NovelRoom> getDeactivateRoom() {
        return novelRoomRepository.findByStatus("DEACTIVE");
    }

    @Transactional
    public void save(NovelRoom novelRoom) {
        novelRoomRepository.saveAndFlush(novelRoom);
    }

    public String getHostUserId(Integer roomId) {
        return novelRoomRepository.findById(roomId)
                .map(NovelRoom::getHostUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 방이 존재하지 않습니다: " + roomId));
    }

    // 소설방 설정 변경
    public void updateNovelRoomSettings(Integer roomId, String updatedTitle, String updatedDescription, String updatedStatus) {

        NovelRoom novelRoom = novelRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("소설방을 찾을 수 없습니다: roomId=" + roomId));

        if (updatedTitle != null && !updatedTitle.trim().isEmpty()) {
            novelRoom.setTitle(updatedTitle);
        }

        if (updatedDescription != null) {
            novelRoom.setDescription(updatedDescription);
        }

        if (updatedStatus != null && (updatedStatus.equals("N"))) {
            novelRoom.setStatus("DEACTIVE");
        }

        novelRoomRepository.save(novelRoom);
    }
}
