package com.monopoly.repository.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.exception.GameSessionSerializationException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

@Convert
public class GameSessionTypeConverter implements AttributeConverter<GameSession, String> {

    private final ObjectMapper objectMapper;

    public GameSessionTypeConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.objectMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, true);
    }

    @Override
    public String convertToDatabaseColumn(GameSession gameSession) {
        if (gameSession == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(gameSession);
        } catch (JsonProcessingException e) {
            throw new GameSessionSerializationException("Ошибка сериализации GameSession: " + e.getMessage(), e);
        }
    }

    @Override
    public GameSession convertToEntityAttribute(String string) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }

        try {
            return objectMapper.readValue(string, GameSession.class);
        } catch (JsonProcessingException e) {
            throw new GameSessionSerializationException("Ошибка десериализации GameSession: " + e.getMessage(), e);
        }
    }
}
