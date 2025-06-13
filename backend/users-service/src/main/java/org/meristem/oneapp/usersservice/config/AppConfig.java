package org.meristem.oneapp.usersservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.config.configProperties.OneAppUsersProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AppConfig {

    @Value("${one-app.server-url:http://localhost:20010/}")
    private String serverUrl;

    @Value("${one-app.server-version}")
    private String serverVersion;

    @Value("${one-app.server-app-name}")
    private String serverAppName;

    @Value("${one-app.email}")
    private String email;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final OneAppUsersProperties oneAppUsersProperties;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ));
        mapper.registerModule(module);
        return mapper;
    }

    @Bean
    public RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(8))
                .disableCachingNullValues();
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer cacheManagerBuilderCustomizer() {
        return builder -> builder

                .withCacheConfiguration("users", defaultCacheConfiguration())
                .withCacheConfiguration("avatars", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(50)))
                .withCacheConfiguration("settings", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)));
    }


    @Bean
    public OpenAPI apiDoclet() {
        Server server = new Server();
        server.setUrl(serverUrl.concat(contextPath));
        server.description("Users API Documentation");

        Contact contact = new Contact().url(serverUrl).email(email).name(serverAppName);
        Info info = new Info().title(serverAppName).version(serverVersion).contact(contact).description("This API exposes endpoints to manage users.");

        final String securitySchemeName = "OAuth2 Security";
        return new OpenAPI().info(info).servers(List.of(server))
                .components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                    .type(SecurityScheme.Type.OAUTH2)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("This API uses OAuth 2 with the implicit grant flow.")
                    .flows(new OAuthFlows().password(new OAuthFlow().tokenUrl(serverUrl.concat(oneAppUsersProperties.contextPath())
                            .concat("/oauth2/token")).scopes(new Scopes().addString("profile", "profile")))
                            .clientCredentials(new OAuthFlow().tokenUrl(serverUrl.concat(oneAppUsersProperties.contextPath())
                                    .concat("/oauth2/token"))))
                )
            ).security(List.of(new SecurityRequirement().addList(securitySchemeName)));
    }
}
