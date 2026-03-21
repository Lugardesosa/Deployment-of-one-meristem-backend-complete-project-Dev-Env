package org.meristem.oneapp.usersservice.config.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;

@Profile({"local"})
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class LocalJdbcConfig extends AbstractJdbcConfiguration {

    private final ObjectMapper mapper;

    @Override
    protected @NonNull List<?> userConverters() {
        return List.of(
                new H2JsonWritingConverter(mapper),
                new H2JsonReadingConverter(mapper)
        );
    }
}
