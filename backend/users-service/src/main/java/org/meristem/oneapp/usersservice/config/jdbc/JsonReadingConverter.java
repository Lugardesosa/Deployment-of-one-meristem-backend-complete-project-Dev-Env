package org.meristem.oneapp.usersservice.config.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
@RequiredArgsConstructor
public class JsonReadingConverter implements Converter<String, JsonNode> {

    private final ObjectMapper mapper;
    @Override
    public @Nullable JsonNode convert(@NonNull String source) {
        try {
            return mapper.readValue(source, JsonNode.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON deserialization failed", e);
        }
    }
}
