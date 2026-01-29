package org.meristem.oneapp.usersservice.config.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
@Getter
@RequiredArgsConstructor
public class H2JsonWritingConverter implements Converter<JsonNode, String> {

    private final ObjectMapper objectMapper;

    @Override
    public @Nullable String convert(@NonNull JsonNode source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON serialization failed", e);
        }
    }
}
