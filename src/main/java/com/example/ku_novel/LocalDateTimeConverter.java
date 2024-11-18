package com.example.ku_novel;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Long> {

    @Override
    public Long convertToDatabaseColumn(LocalDateTime attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        return LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(dbData), ZoneOffset.UTC);
    }
}


