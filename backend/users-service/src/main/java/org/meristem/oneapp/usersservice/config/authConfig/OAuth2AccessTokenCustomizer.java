package org.meristem.oneapp.usersservice.config.authConfig;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class OAuth2AccessTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final RegisteredClientRepository registeredClientRepository;
    @Override
    public void customize(JwtEncodingContext context) {

        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            context.getClaims().claims(claim -> {
                Object principal = context.getPrincipal().getPrincipal();
                if (principal instanceof AuthenticatedUser users) {
                    Set<String> roles = AuthorityUtils.authorityListToSet(users.getAuthorities());
                    claim.put("roles", roles);
                    claim.put("isAdmin", roles.stream().noneMatch(role -> role.equals(AppConstants.USER_ROLE)));
                    claim.put("sub", users.getEmail());
                    claim.put("email", users.getEmail());
                    claim.put("firstName", users.getFirstName());
                    claim.put("lastName", users.getLastName());
                    claim.put("id", users.getId());
                    claim.put("phoneNumber", users.getPhoneNumber());
                    claim.put("status", users.getStatus());
                } else if (principal instanceof String) {
                    RegisteredClient rc = requireNonNull(registeredClientRepository.findByClientId((String) principal), "Client not found");
                    claim.put("client_id", rc.getClientId());
                    claim.put("scope", rc.getScopes());
                }
            });
        }
    }
}
