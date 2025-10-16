package org.meristem.oneapp.cloudgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public KeyResolver keyResolver(@Value("${whitelisted.ips}") Set<String> whitelistedIps) {
        return (exchange) -> Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .filter(ip -> !whitelistedIps.contains(ip))
                .map(Mono::just)
                .orElse(Mono.empty());
    }
}
