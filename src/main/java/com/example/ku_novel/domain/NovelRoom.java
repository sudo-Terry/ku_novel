package com.example.ku_novel.domain;

import com.example.ku_novel.LocalDateTimeConverter;
import com.example.ku_novel.common.Message;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "novel_room")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NovelRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment 설정
    @Column(name = "room_id")
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "participant_ids")
    private String participantIds; // JSON 형식 또는 구분자 방식으로 저장

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "novel_content")
    private String novelContent; // JSON 형식 또는 구분자 방식으로 저장

    @Column(name = "host_user_id", nullable = false)
    private String hostUserId;

    @Column(name = "current_vote_id")
    private Integer currentVoteId;

    @Column(name = "submission_duration", nullable = false)
    private Integer submissionDuration; // 소설가 입력 시간 (분 단위)

    @Column(name = "voting_duration", nullable = false)
    private Integer votingDuration;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() { // 디버깅용으로 데이터 출력해보기 위해 추가
        return "NovelRoom{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", participantIds='" + participantIds + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", novelContent='" + novelContent + '\'' +
                ", hostUserId='" + hostUserId + '\'' +
                ", currentVoteId=" + currentVoteId +
                ", submissionDuration=" + submissionDuration +
                ", votingDuration=" + votingDuration +
                '}';
    }

    public Message toMessage() {
        Message message = new Message();
        message.setNovelRoomId(this.id);
        message.setNovelRoomTitle(this.title);
        message.setNovelRoomDescription(this.description);
        message.setNovelRoomStatus(this.status);
        message.setNovelHostUser(this.hostUserId);
        message.setNovelParticipantIds(this.getParticipantIdsAsList());
        message.setVotingDuration(this.votingDuration);
        message.setSubmissionDuration(this.submissionDuration);
        message.setMaxParticipants(this.maxParticipants);
        message.setNovelContent(this.novelContent);
        return message;
    }

    public List<String> getParticipantIdsAsList() {
        if (participantIds == null || participantIds.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(participantIds.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public void setParticipantIdsFromList(List<String> participantIdsList) {
        if (participantIdsList == null || participantIdsList.isEmpty()) {
            this.participantIds = "";
        } else {
            this.participantIds = String.join(",", participantIdsList);
        }
    }
}