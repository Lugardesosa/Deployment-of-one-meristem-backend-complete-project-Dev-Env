package org.meristem.oneapp.usersservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.meristem.oneapp.usersservice.config.configProperties.OneAppUsersProperties;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.MappingException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.util.backoff.FixedBackOff;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class AppConfig {

    public static final int CACHE_SIZE = 10000;

    @Bean
    public JdbcTemplateLockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .usingDbTime()
                .build());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return AppUtil.getMapper();
    }

    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withCache(CACHE_SIZE)
                .build();
    }


    @Bean
    public OpenAPI apiDoclet(OneAppUsersProperties oneAppUsersProperties, @Value("${one-app.server-url:http://localhost:20010/}")
                             String serverUrl, @Value("${one-app.server-version}") String serverVersion, @Value("${one-app.server-app-name}") String serverAppName,
                             @Value("${one-app.email}") String email, @Value("${server.servlet.context-path}") String contextPath) {
        Server server = new Server();
        server.setUrl(serverUrl.concat(contextPath));
        server.description("Users API Documentation");

        Contact contact = new Contact().url(serverUrl).email(email).name(serverAppName);
        Info info = new Info().title(serverAppName).version(serverVersion).contact(contact).description("This API exposes endpoints to manage users.");

        final String securitySchemeName = "OAuth2 Security";
        String url = serverUrl.concat(oneAppUsersProperties.contextPath())
                .concat("/oauth2/token");
        return new OpenAPI().info(info).servers(List.of(server))
                .components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                    .type(SecurityScheme.Type.OAUTH2)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("This API uses OAuth 2 with the implicit grant flow.")
                    .flows(new OAuthFlows().password(new OAuthFlow().tokenUrl(url).scopes(new Scopes().addString("profile", "profile")))
                            .clientCredentials(new OAuthFlow().tokenUrl(url)))
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

        handler.setClassifications(Map.of(IllegalArgumentException.class,  false, MappingException.class, false, NullPointerException.class , false, BadRequestException.class, false), true);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("Failed to process {} after {} attempts", record, deliveryAttempt, ex);
        });
        return handler;
    }
}
