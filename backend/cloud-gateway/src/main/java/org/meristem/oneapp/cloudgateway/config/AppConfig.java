package org.meristem.oneapp.cloudgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    @Bean
    @Primary
    public SafeXForwardedHeadersFilter xForwardedHeadersFilter() {
        return new SafeXForwardedHeadersFilter("10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|192\\.168\\.\\d{1,3}\\.\\d{1,3}|169\\.254\\.\\d{1,3}\\.\\d{1,3}|127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|100\\.6[4-9]{1}\\.\\d{1,3}\\.\\d{1,3}|100\\.[7-9]{1}\\d{1}\\.\\d{1,3}\\.\\d{1,3}|100\\.1[0-1]{1}\\d{1}\\.\\d{1,3}\\.\\d{1,3}|100\\.12[0-7]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}|0:0:0:0:0:0:0:1|::1|fe[89ab]\\p{XDigit}:.*|f[cd]\\p{XDigit}{2}:.*+");
    }
}
