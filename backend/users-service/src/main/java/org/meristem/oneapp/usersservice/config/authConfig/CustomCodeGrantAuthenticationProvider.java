package org.meristem.oneapp.usersservice.config.authConfig;

import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.ErrorMessages;
import org.meristem.oneapp.usersservice.domains.enums.UserStatus;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Slf4j
@Component
public class CustomCodeGrantAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private final JdbcOAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final RedisCacheManager cacheManager;

    public CustomCodeGrantAuthenticationProvider(JdbcOAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                                 CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UsersRepository usersRepository, RedisCacheManager cacheManager) {
        Assert.notNull(authorizationService, "oAuth2AuthorizationService must not be null");
        Assert.notNull(tokenGenerator, "oAuth2TokenGenerator must not be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomCodeGrantAuthenticationToken token = (CustomCodeGrantAuthenticationToken) authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = CustomOAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(token);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        String username = token.getUsername();
        String password = token.getPassword();
        Set<String> scopes = token.getScopes();

        AuthenticatedUser user;
        try {
            user = (AuthenticatedUser) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, ErrorMessages.INVALID_USERNAME, null));
        }

        if (user.getStatus() == UserStatus.LOCKED.getValue()) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, ErrorMessages.ACCOUNT_LOCKED, null));
        }

        if (user.getStatus() == UserStatus.DEACTIVATED.getValue()) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, ErrorMessages.ACCOUNT_DEACTIVATED, null));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            usersRepository.updatePasswordAttempt(user.getEmail(), user.getPasswordAttempt() + 1);
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(user.getId());

            if (user.getPasswordAttempt() + 1 == AppConstants.PASSWORD_ATTEMPTS) {
                usersRepository.updateStatus( user.getEmail(), UserStatus.LOCKED.getValue());
                requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(user.getId());
            }

            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
                    String.format(ErrorMessages.INVALID_PASSWORD, (AppConstants.PASSWORD_ATTEMPTS - (user.getPasswordAttempt() + 1))),
                    null));
        } else {
            usersRepository.updatePasswordAttempt(user.getEmail(), 0);
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(user.getId());
        }
        if (registeredClient == null || !registeredClient.getAuthorizationGrantTypes().contains(token.getGrantType())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
        scopes.forEach(s -> {
            if (!registeredClient.getScopes().contains(s)) {
                throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
            }
        });

        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        DefaultOAuth2TokenContext.Builder contextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(scopes)
                .authorizationGrantType(token.getGrantType())
                .authorizationGrant(token);

        OAuth2TokenContext tokenContext = contextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (isNull(generatedAccessToken)) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, ErrorMessages.TOKEN_COULD_NOT_BE_GENERATED, null));
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, generatedAccessToken.getTokenValue(),
                generatedAccessToken.getIssuedAt(), generatedAccessToken.getExpiresAt(), null);
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .authorizationGrantType(token.getGrantType());

        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, metadata -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                    ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {
            tokenContext = contextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (generatedRefreshToken instanceof OAuth2RefreshToken oAuth2RefreshToken) {
                authorizationBuilder.refreshToken(oAuth2RefreshToken);
                refreshToken = oAuth2RefreshToken;
            } else {
                throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, ErrorMessages.REFRESH_TOKEN_COULD_NOT_BE_GENERATED, ERROR_URI));
            }
        }

        OAuth2Authorization authorization = authorizationBuilder
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authorizedScopes(scopes)
                .attribute(Principal.class.getName(), usernamePasswordAuthenticationToken)
                .build();
        this.authorizationService.save(authorization);
        log.info("user - {} logged in successfully at {}", user.getEmail(), LocalDateTime.now());
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, usernamePasswordAuthenticationToken, accessToken, refreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomCodeGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
