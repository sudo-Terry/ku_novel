package com.example.ku_novel.dto;

import com.example.ku_novel.domain.NovelRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class NovelRoomResDto {

    private Integer id;
    private String title;
    private String description;
    private String createdAt;
    private String participantIds;
    private Integer maxParticipants;
    private String status;

    public NovelRoomResDto(NovelRoom room) {
        this.id = room.getId();
        this.title = room.getTitle();
        this.description = room.getDescription();
        this.createdAt = room.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.participantIds = room.getParticipantIds();
        this.maxParticipants = room.getMaxParticipants();
        this.status = room.getStatus();
    }

}
