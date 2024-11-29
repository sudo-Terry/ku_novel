package com.example.ku_novel;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value != null) {
            out.value(value.format(FORMATTER));
        } else {
            out.nullValue(); // null 처리
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in != null) {
            return LocalDateTime.parse(in.nextString(), FORMATTER); // 문자열을 LocalDateTime으로 역직렬화
        } else {
            return null;
        }
    }
}

