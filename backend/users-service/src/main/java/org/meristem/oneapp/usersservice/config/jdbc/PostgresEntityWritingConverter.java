package org.meristem.oneapp.usersservice.config.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.sql.SQLException;

@WritingConverter
@Getter
@RequiredArgsConstructor
public class PostgresEntityWritingConverter implements Converter<JsonNode, PGobject> {

    private final ObjectMapper objectMapper;

    @Override
    public @Nullable PGobject convert(@NonNull JsonNode source) {
        try {
            String jsonString = objectMapper.writeValueAsString(source);
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(jsonString);
            return pgObject;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON serialization failed", e);
        } catch (SQLException e) {
            throw new BadRequestException("Request could not be completed");

        }
    }
}
