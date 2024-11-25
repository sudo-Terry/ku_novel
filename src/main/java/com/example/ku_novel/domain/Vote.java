package com.example.ku_novel.domain;

import com.example.ku_novel.LocalDateTimeConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment 설정
    @Column(name = "vote_id")
    private Integer id;

    @Column(name = "novelroom_id")
    private Integer novelRoomId;

    @Column(name = "content_options")
    private String contentOptions;

    @Column(name = "votes")
    private String votes;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
