package com.example.ku_novel.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "novel_room")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NovelRoom {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment 설정 -> 버그가 있어서 주석처리
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "participant_ids")
    private String participantIds; // JSON 형식 또는 구분자 방식으로 저장

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "novel_content")
    private String novelContent; // JSON 형식 또는 구분자 방식으로 저장

    @Column(name = "host_user_id", nullable = false)
    private String hostUserId;

    @Column(name = "current_vote_id")
    private Integer currentVoteId;
}