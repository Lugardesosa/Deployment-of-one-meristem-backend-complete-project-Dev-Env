package org.meristem.oneapp.coreservice.config;

import org.meristem.oneapp.coreservice.utils.AppUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

import java.util.Optional;

@Configuration
@EnableJdbcAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(AppUtil.getLoggedInSubject());
    }
}
