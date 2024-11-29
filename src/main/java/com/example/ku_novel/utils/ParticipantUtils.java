package com.example.ku_novel.utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticipantUtils {

    private static final Gson gson = new Gson();
    private static final Type listType = new TypeToken<List<String>>() {}.getType();

    // JSON 문자열에서 List<String>으로 변환
    public static List<String> parseParticipantIds(String participantIdsJson) {
        if (participantIdsJson == null || participantIdsJson.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            // JSON 배열 처리
            return gson.fromJson(participantIdsJson, listType);
        } catch (Exception e) {
            // 쉼표로 구분된 문자열 처리
            if (participantIdsJson.startsWith("\"") && participantIdsJson.endsWith("\"")) {
                participantIdsJson = participantIdsJson.substring(1, participantIdsJson.length() - 1); // 양 끝 따옴표 제거
            }
            if (participantIdsJson.contains(",")) {
                return Arrays.asList(participantIdsJson.split(","));
            }
            // 하나의 ID만 포함된 경우
            return List.of(participantIdsJson);
        }
    }

    // List<String>을 JSON 문자열로 변환
    public static String toParticipantIdsJson(List<String> participantIds) {
        try {
            return gson.toJson(participantIds);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert participant IDs to JSON", e);
        }
    }

    // Participant 추가 함수
    public static String addParticipant(String participantIdsJson, String newParticipantId) {
        List<String> participantIds = parseParticipantIds(participantIdsJson);
        if (!participantIds.contains(newParticipantId)) {
            participantIds.add(newParticipantId);
        }
        return toParticipantIdsJson(participantIds);
    }

    // Participant 제거 함수 (안씀)
//    public static String removeParticipant(String participantIdsJson, String participantIdToRemove) {
//        List<String> participantIds = parseParticipantIds(participantIdsJson);
//        participantIds.remove(participantIdToRemove);
//        return toParticipantIdsJson(participantIds);
//    }
}
