package com.example.ku_novel.domain;

import com.example.ku_novel.LocalDateTimeConverter;
import com.example.ku_novel.common.Message;
import com.example.ku_novel.utils.ParticipantUtils;
import com.example.ku_novel.utils.VoteUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(name = "WRITER_ENABLED", nullable = false)
    private String status = "VOTE_COMPLETED";

    @Column(name = "submission_duration", nullable = false)
    private Integer submissionDuration; // 소설가 입력 시간 (분 단위)

    @Column(name = "voting_duration", nullable = false)
    private Integer votingDuration;

    public Message toMessage() {
        Message message = new Message();
        message.setNovelVoteId(this.id);
        message.setVotingDuration(this.votingDuration);
        message.setSubmissionDuration(this.submissionDuration);
        message.setNovelRoomId(this.novelRoomId);
        message.setContentOptions(this.getContentOptions());
        message.setVotes(this.votes);
        message.setVoteStatus(this.status);
        message.setCountDown(VoteUtils.getCountDown(createdAt, submissionDuration, votingDuration));
        return message;
    }

    public List<String> getContentOptions() {
        return VoteUtils.parseContentOptions(this.contentOptions);
    }
}
