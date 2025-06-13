package org.meristem.oneapp.notificationservice.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class AppConfig {


    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Africa/Lagos")));
    }
}
