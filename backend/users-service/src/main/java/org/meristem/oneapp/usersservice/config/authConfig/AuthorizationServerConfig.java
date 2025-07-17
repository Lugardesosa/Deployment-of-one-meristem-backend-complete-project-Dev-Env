package org.meristem.oneapp.usersservice.config.authConfig;


import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.config.configProperties.RsaKeys;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AuthorizationServerConfig {

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JdbcTemplate jdbcTemplate, JdbcOperations jdbcOperations, RsaKeys rsaKeys,
                                                   CustomUserDetailsService userDetailsService, UsersRepository usersRepository) throws Exception {
        OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();
        http.securityMatcher(configurer.getEndpointsMatcher())
                .with(configurer, cfgr ->
                        cfgr.oidc(Customizer.withDefaults())
                                .tokenEndpoint(te -> te.accessTokenRequestConverter(new CustomPasswordAuthenticationConverter())
                                        .authenticationProvider(new CustomCodeGrantAuthenticationProvider(oAuth2AuthorizationService(jdbcOperations, jdbcTemplate),
                                                tokenGenerator(jdbcTemplate, rsaKeys), userDetailsService, passwordEncoder(), usersRepository)
                                        )
                                )
                );
        return http.build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, ObjectMapper mapper, CustomJwtConverter customJwtConverter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(requests -> requests.requestMatchers("/h2-console/**", "/oauth/token", "/webjars/**", "/swagger-ui/**", "/actuator/**", "/api-docs/**", "/ws/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/notification/otp", "/notification/otp/verify", "/base", "/base/password-reset", "/onboard/smile-id/webhook", "/onboard/okhi/webhook").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtConverter));
                    oauth2.authenticationEntryPoint(new RestAuthenticationEntryPoint(mapper));
                    oauth2.accessDeniedHandler(new RestAccessDeniedException(mapper));
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


    @Bean
    OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JdbcTemplate jdbcTemplate, RsaKeys rsaKeys) {
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource(rsaKeys));
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        OAuth2AccessTokenCustomizer customizer = new OAuth2AccessTokenCustomizer(clientRepository(jdbcTemplate), jdbcTemplate);
        jwtGenerator.setJwtCustomizer(customizer);
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    JWKSource<SecurityContext> jwkSource(RsaKeys rsaKeys) {

        RSAPublicKey publicKey = rsaKeys.publicKey();
        RSAPrivateKey privateKey = rsaKeys.privateKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings(
            @Value("${one-app.users-service.context-path}") String usersServiceContextPath,
            @Value("${one-app.server-url:http://localhost:20010/}") String serverUrl) {
        return AuthorizationServerSettings.builder().issuer(serverUrl.concat(usersServiceContextPath)).build();
    }

    @Bean
    JdbcOAuth2AuthorizationService oAuth2AuthorizationService(JdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        var jdbcOAuth2AuthorizationService = new JdbcOAuth2AuthorizationService(jdbcOperations, clientRepository(jdbcTemplate));

        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(clientRepository(jdbcTemplate));
        ObjectMapper mapper1 = new ObjectMapper();
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModule = SecurityJackson2Modules.getModules(classLoader);
        mapper1.registerModules(securityModule);
        mapper1.registerModule(new OAuth2AuthorizationServerJackson2Module());

        mapper1.addMixIn(Long.class, LongMixin.class);

        rowMapper.setObjectMapper(mapper1);
        jdbcOAuth2AuthorizationService.setAuthorizationRowMapper(rowMapper);
        return jdbcOAuth2AuthorizationService;
    }

    @Bean
    JdbcRegisteredClientRepository clientRepository(JdbcTemplate jdbcTemplate) {
//        RegisteredClient mobile = RegisteredClient
//                .withId(UUID.randomUUID().toString())
//                .clientId("mobile-service")
//                .clientSecret(passwordEncoder().encode("secret"))
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .redirectUri("localhost:30000/login/oauth2/code/mobile-service")
//                .postLogoutRedirectUri("localhost:30000/logout")
//                .scope(OidcScopes.OPENID)
//                .scopes(e -> e.addAll(List.of("user.read", "user.write")))
//                .scope(OidcScopes.PROFILE)
//                .scope(OidcScopes.EMAIL)
//                .tokenSettings(TokenSettings.builder().refreshTokenTimeToLive(Duration.ofDays(15))
//                        .reuseRefreshTokens(false).accessTokenTimeToLive(Duration.ofMinutes(5)).build())
//                .build();
//        RegisteredClient users = RegisteredClient
//                .withId(UUID.randomUUID().toString())
//                .clientId("users-service")
//                .clientSecret(passwordEncoder().encode("secret"))
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .scope(OidcScopes.OPENID)
//                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build())
//                .build();
//        RegisteredClient notifications = RegisteredClient
//                .withId(UUID.randomUUID().toString())
//                .clientId("notification-service")
//                .clientSecret(passwordEncoder().encode("secret"))
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//                .scope(OidcScopes.EMAIL)
//                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build())
//                .build();

        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
//        registeredClientRepository.save(mobile);
//        registeredClientRepository.save(users);
//        registeredClientRepository.save(notifications);
        return registeredClientRepository;
    }
}
