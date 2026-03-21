package org.meristem.oneapp.notificationservice.config;

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
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.notificationservice.exception.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.mapping.MappingException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.client.ResourceAccessException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule module = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        mapper.registerModule(module);
        return mapper;
    }

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Africa/Lagos")));
    }


    @Bean
    public Map<String, String> textMessages(ResourcePatternResolver resolver) throws IOException {

        Map<String, String> messages = new HashMap<>();
        Resource[] resources = resolver.getResources("classpath*:sms-templates/*.txt");

        for (Resource resource : resources) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                messages.put(Objects.requireNonNull(resource.getFilename()).replace(".txt", ""), sb.toString());
            }
        }
        return messages;
    }

    @Bean
    public OpenAPI apiDoclet(@Value("${one-app.server-url:http://localhost:20010/}") String serverUrl,
                             @Value("${one-app.server-version}") String serverVersion, @Value("${one-app.server-app-name}")
                             String serverAppName, @Value("${one-app.email}") String email, @Value("${server.servlet.context-path}")
                             String contextPath, @Value("${one-app.users-service.context-path}") String usersServiceContextPath
    ) {
        Server server = new Server();
        server.setUrl(serverUrl.concat(contextPath));
        server.description("Notification API Documentation");

        Contact contact = new Contact().url(serverUrl).email(email).name(serverAppName);
        Info info = new Info().title(serverAppName).version(serverVersion).contact(contact).description("This API exposes endpoints to manage and interact with notification service.");

        final String securitySchemeName = "OAuth2 Security";
        return new OpenAPI().info(info).servers(List.of(server))
                .components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.OAUTH2)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("This API uses OAuth 2 with the implicit grant flow.")
                                .flows(new OAuthFlows().password(new OAuthFlow().tokenUrl(serverUrl.concat(usersServiceContextPath)
                                                .concat("/oauth2/token")).scopes(new Scopes().addString("profile", "profile")))
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

        handler.setClassifications(
                Map.ofEntries(
                        Map.entry(IllegalArgumentException.class, false),
                        Map.entry(MappingException.class, false),
                        Map.entry(NullPointerException.class, false),
                        Map.entry(BadRequestException.class, false),
                        Map.entry(ResourceAccessException.class, false),
                        Map.entry(DeserializationException.class, false),
                        Map.entry(MessageConversionException.class, false),
                        Map.entry(ConversionException.class, false),
                        Map.entry(MethodArgumentResolutionException.class, false),
                        Map.entry(NoSuchMethodException.class, false),
                        Map.entry(ClassCastException.class, false)
                ), true);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> log.warn("Failed to process {} after {} attempts", record, deliveryAttempt, ex));
        return handler;
    }
}
