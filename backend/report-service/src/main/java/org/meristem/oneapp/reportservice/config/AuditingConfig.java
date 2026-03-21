package org.meristem.oneapp.reportservice.config;


import org.meristem.oneapp.reportservice.utils.AppUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

import java.util.Optional;

@Configuration
@EnableJdbcAuditing
public class AuditingConfig {

    @Bean
    AuditorAware<String> auditorProvider() {
        return () -> Optional.of(AppUtil.getLoggedInSubject());
    }
}
