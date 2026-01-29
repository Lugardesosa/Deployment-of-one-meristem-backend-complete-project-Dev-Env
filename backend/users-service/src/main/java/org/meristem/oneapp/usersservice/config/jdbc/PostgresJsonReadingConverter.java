package org.meristem.oneapp.usersservice.config.jdbc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

//@Profile({"dev", "prod"})
@ReadingConverter
@RequiredArgsConstructor
public class PostgresJsonReadingConverter implements Converter<PGobject, JsonNode> {

    private final ObjectMapper mapper;
    @Override
    public @Nullable JsonNode convert(@NonNull PGobject source) {
        try {
            String json = source.getValue();
            return mapper.valueToTree(json);
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON deserialization failed", e);
        }
    }
}
