package org.meristem.oneapp.walletservice.config;

import org.meristem.oneapp.walletservice.constants.AppConstants;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer cacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(AppConstants.WALLET_CACHE_NAME, defaultCacheConfiguration().entryTtl(Duration.ofHours(5)));
    }

    @Bean
    public RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(8))
                .disableCachingNullValues();
    }
}
