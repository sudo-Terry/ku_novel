package com.example.ku_novel.domain;

import com.example.ku_novel.LocalDateTimeConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

}