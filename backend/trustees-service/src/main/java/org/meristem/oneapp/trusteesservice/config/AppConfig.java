package org.meristem.oneapp.trusteesservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trusteesservice.dtos.Serializer.BigDecimalTwoDecimalSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(
                LocalDateTime.class, new LocalDateTimeSerializer(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                )
        );
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, new BigDecimalTwoDecimalSerializer());
        mapper.registerModule(javaTimeModule);
        mapper.registerModule(simpleModule);
        return mapper;
    }

    @Bean
    public RedisCacheConfiguration defaultRedisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(1));
    }

    @Bean
    public OpenAPI apiDoclet(@Value("${one-app.server-url:http://localhost:20010/}") String serverUrl,
                             @Value("${one-app.server-version}") String serverVersion, @Value("${one-app.server-app-name}")
                             String serverAppName, @Value("${one-app.email}") String email, @Value("${server.servlet.context-path}")
                             String contextPath, @Value("${one-app.users-service.context-path}") String usersServiceContextPath
    ) {
        Server server = new Server();
        server.setUrl(serverUrl.concat(contextPath));
        server.description("Trustees API Documentation");

        Contact contact = new Contact().url(serverUrl).email(email).name(serverAppName);
        Info info = new Info().title(serverAppName).version(serverVersion).contact(contact).description("This API exposes endpoints to manage and interact with users' wallets.");

        final String securitySchemeName = "OAuth2 Security";
        return new OpenAPI().info(info).servers(List.of(server))
                .components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.OAUTH2)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("This API uses OAuth 2 with the implicit grant flow.")
                                .flows(new OAuthFlows().password(new OAuthFlow().tokenUrl(serverUrl.concat(usersServiceContextPath)
                                                .concat("/oauth2/token")).scopes(new Scopes().addString("openid", "openid")))
                                        .clientCredentials(new OAuthFlow().tokenUrl(serverUrl.concat(usersServiceContextPath)
                                                .concat("/oauth2/token"))))
                        )
                ).security(List.of(new SecurityRequirement().addList(securitySchemeName)));
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        DefaultErrorHandler handler = new DefaultErrorHandler(
                recoverer,
                new FixedBackOff(2000L, 3)
        );

        handler.addNotRetryableExceptions(IllegalArgumentException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("Failed to process {} after {} attempts", record, deliveryAttempt, ex);
        });
        return handler;
    }
}
