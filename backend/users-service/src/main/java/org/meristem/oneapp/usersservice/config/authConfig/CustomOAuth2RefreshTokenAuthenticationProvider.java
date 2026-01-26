
package org.meristem.oneapp.usersservice.config.authConfig;

import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public final class CustomOAuth2RefreshTokenAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private final Log logger = LogFactory.getLog(getClass());

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public CustomOAuth2RefreshTokenAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2RefreshTokenAuthenticationToken refreshTokenAuthentication = (OAuth2RefreshTokenAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = CustomOAuth2AuthenticationProviderUtils
                .getAuthenticatedClientElseThrowInvalidClient(refreshTokenAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Retrieved registered client");
        }

        OAuth2Authorization authorization = this.authorizationService
                .findByToken(refreshTokenAuthentication.getRefreshToken(), OAuth2TokenType.REFRESH_TOKEN);
        if (authorization == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        assert registeredClient != null;
        if (!registeredClient.getId().equals(authorization.getRegisteredClientId())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        assert refreshToken != null;
        if (!refreshToken.isActive()) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        Set<String> scopes = refreshTokenAuthentication.getScopes();
        Set<String> authorizedScopes = authorization.getAuthorizedScopes();
        if (!authorizedScopes.containsAll(scopes)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
        }

        // Verify the DPoP Proof (if available)
        Jwt dPoPProof = CustomDPoPProofVerifier.verifyIfAvailable(refreshTokenAuthentication);

        if (dPoPProof != null
                && clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            Map<String, Object> accessTokenClaims = authorization.getAccessToken().getClaims();
            verifyDPoPProofPublicKey(dPoPProof, () -> accessTokenClaims);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Validated token request parameters");
        }

        if (scopes.isEmpty()) {
            scopes = authorizedScopes;
        }

        // @formatter:off
		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient)
				.principal(authorization.getAttribute(Principal.class.getName()))
				.authorizationServerContext(AuthorizationServerContextHolder.getContext())
				.authorization(authorization)
				.authorizedScopes(scopes)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrant(refreshTokenAuthentication);
		// @formatter:on
        if (dPoPProof != null) {
            tokenContextBuilder.put(OAuth2TokenContext.DPOP_PROOF_KEY, dPoPProof);
        }

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization);

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = CustomOAuth2AuthenticationProviderUtils.accessToken(authorizationBuilder,
                generatedAccessToken, tokenContext);

        // ----- Refresh token -----
        OAuth2RefreshToken currentRefreshToken = refreshToken.getToken();
        if (!registeredClient.getTokenSettings().isReuseRefreshTokens()) {
            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            currentRefreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(currentRefreshToken);
        }

        authorization = authorizationBuilder.build();

        this.authorizationService.save(authorization);
        log.info("user {} refresh token request with biometric ({}) successful", refreshTokenAuthentication.getAdditionalParameters().get("email"), refreshTokenAuthentication.getAdditionalParameters().get("isBiometric"));
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
                currentRefreshToken, Collections.emptyMap());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void verifyDPoPProofPublicKey(Jwt dPoPProof, ClaimAccessor accessTokenClaims) {
        JWK jwk = null;
        @SuppressWarnings("unchecked")
        Map<String, Object> jwkJson = (Map<String, Object>) dPoPProof.getHeaders().get("jwk");
        try {
            jwk = JWK.parse(jwkJson);
        } catch (Exception ignored) {
        }
        if (jwk == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_DPOP_PROOF,
                    "jwk header is missing or invalid.", null);
            throw new OAuth2AuthenticationException(error);
        }

        String jwkThumbprint;
        try {
            jwkThumbprint = jwk.computeThumbprint().toString();
        } catch (Exception ex) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_DPOP_PROOF,
                    "Failed to compute SHA-256 Thumbprint for jwk.", null);
            throw new OAuth2AuthenticationException(error);
        }

        String jwkThumbprintClaim = null;
        Map<String, Object> confirmationMethodClaim = accessTokenClaims.getClaimAsMap("cnf");
        if (!CollectionUtils.isEmpty(confirmationMethodClaim) && confirmationMethodClaim.containsKey("jkt")) {
            jwkThumbprintClaim = (String) confirmationMethodClaim.get("jkt");
        }
        if (jwkThumbprintClaim == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_DPOP_PROOF, "jkt claim is missing.", null);
            throw new OAuth2AuthenticationException(error);
        }

        if (!jwkThumbprint.equals(jwkThumbprintClaim)) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_DPOP_PROOF, "jwk header is invalid.", null);
            throw new OAuth2AuthenticationException(error);
        }
    }
}
