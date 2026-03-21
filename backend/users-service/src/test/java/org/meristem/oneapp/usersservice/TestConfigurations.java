package org.meristem.oneapp.usersservice;


import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@TestConfiguration
@EnableJdbcRepositories
public class TestConfigurations {

    @Bean
    public RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(8))
                .disableCachingNullValues();
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer cacheManagerBuilderCustomizer() {
        return builder -> builder

                .withCacheConfiguration(AppConstants.USERS_CACHE_NAME, defaultCacheConfiguration())
                .withCacheConfiguration(AppConstants.AVATAR_CACHE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(50)))
                .withCacheConfiguration(AppConstants.SIGN_UP_CACHE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(30)))
                .withCacheConfiguration(AppConstants.SETTINGS_CACHE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)));
    }
}
