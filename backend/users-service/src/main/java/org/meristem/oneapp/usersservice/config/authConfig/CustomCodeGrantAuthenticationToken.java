package org.meristem.oneapp.usersservice.config.authConfig;

import lombok.Getter;
import org.meristem.oneapp.usersservice.domains.enums.AccountType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.isNull;

public class CustomCodeGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 19189291281L;

    @Getter
    private String username;
    @Getter
    private String password;

    @Getter
    private AccountType accountType;
    private final String scopes;
    /**
     * Sub-class constructor.
     *
     * @param authorizationGrantType the authorization grant type
     * @param clientPrincipal        the authenticated client principal
     * @param additionalParameters   the additional parameters
     */
    protected CustomCodeGrantAuthenticationToken(String authorizationGrantType, Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(authorizationGrantType), clientPrincipal, additionalParameters);
        this.username = additionalParameters.get(OAuth2ParameterNames.USERNAME).toString();
        this.password = additionalParameters.get(OAuth2ParameterNames.PASSWORD).toString();
        this.scopes = additionalParameters.get(OAuth2ParameterNames.SCOPE).toString();
        Object loginType = additionalParameters.get("login_type");
        if (isNull(loginType)) {
            throw new OAuth2AuthenticationException("Invalid login type");
        }

        this.accountType = AccountType.fromString(loginType.toString());
        if (isNull(scopes)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
        }
        if (!List.of(AccountType.INDIVIDUAL, AccountType.JOINT).contains(accountType)) {
            throw new OAuth2AuthenticationException("Invalid login type");
        }
    }

    protected Set<String> getScopes() {
        return StringUtils.commaDelimitedListToSet(scopes.replace(" ", ""));
    }
}
