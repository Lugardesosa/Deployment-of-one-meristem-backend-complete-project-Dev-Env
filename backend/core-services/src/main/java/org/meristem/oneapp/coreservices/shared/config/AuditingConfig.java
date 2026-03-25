package org.meristem.oneapp.coreservices.shared.config;

import org.meristem.oneapp.coreservices.shared.utils.SharedAppUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

import java.util.Optional;

@Configuration(proxyBeanMethods = false)
@EnableJdbcAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(SharedAppUtil.getLoggedInSubject());
    }
}
