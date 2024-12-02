package com.example.ku_novel.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.time.Duration;

public class VoteUtils {

    private static final Gson gson = new Gson();
    private static final Type listType = new TypeToken<List<String>>() {
    }.getType();

    public static int getCountDown(LocalDateTime createdAt, int authorWriteMinutes, int votingMinutes) {
        if(createdAt == null){
            return 0;
        }

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 작성 시간이 끝나는 시점
        LocalDateTime authorEndTime = createdAt.plusMinutes(authorWriteMinutes);

        // 투표가 끝나는 시점
        LocalDateTime votingEndTime = authorEndTime.plusMinutes(votingMinutes);

        // 현재 시간이 투표가 끝난 시간보다 늦으면 0을 반환 (남은 시간이 없음)
        if (now.isAfter(votingEndTime)) {
            return 0;
        }

        // 현재 시간이 작성 시간보다 빠르면 작성 시간 종료까지 남은 초 반환
        if (now.isBefore(authorEndTime)) {
            return (int) Duration.between(now, authorEndTime).getSeconds();
        }

        // 현재 시간이 작성 시간 이후, 투표 시간 내라면 투표 종료까지 남은 초 반환
        return (int) Duration.between(now, votingEndTime).getSeconds();
    }

//    public static String getVoteStatus(LocalDateTime createdAt, int authorWriteMinutes, int votingMinutes) {
//        if (createdAt == null) {
//            return "WRITER_ENABLED"; // 소설가 작성 가능
//        }
//
//        // 소설가 작성 종료 시간 계산
//        LocalDateTime authorWriteEndTime = createdAt.plusMinutes(authorWriteMinutes);
//        // 투표 종료 시간 계산
//        LocalDateTime votingEndTime = authorWriteEndTime.plusMinutes(votingMinutes);
//
//        LocalDateTime now = LocalDateTime.now();
//
//        if (now.isBefore(authorWriteEndTime)) {
//            // 소설가 작성 가능 시간 이내
//            return "WRITER_ENABLED";
//        } else if (now.isBefore(votingEndTime)) {
//            // 투표 가능 시간 이내
//            return "VOTING_ENABLED";
//        } else {
//
//            return "VOTE_COMPLETED";
//        }
//    }
}
