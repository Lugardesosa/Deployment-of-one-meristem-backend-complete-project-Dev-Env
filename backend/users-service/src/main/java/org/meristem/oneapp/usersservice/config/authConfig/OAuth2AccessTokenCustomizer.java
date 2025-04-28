package org.meristem.oneapp.usersservice.config.authConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class OAuth2AccessTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final RegisteredClientRepository registeredClientRepository;
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void customize(JwtEncodingContext context) {

        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            context.getClaims().claims(claim -> {
                Object principal = context.getPrincipal().getPrincipal();
                if (principal instanceof AuthenticatedUser users) {
                    Set<String> roles = AuthorityUtils.authorityListToSet(users.getAuthorities());
                    claim.put("roles", roles);
                    claim.put("sub", users.getEmail());
                    claim.put("email", users.getEmail());
                    claim.put("firstName", users.getFirstName());
                    claim.put("lastName", users.getLastName());
                    claim.put("id", users.getId());
                } else if (principal instanceof String) {
                    RegisteredClient rc = requireNonNull(registeredClientRepository.findByClientId((String) principal), "Client not found");
                    String query = "SELECT aud_id FROM audiences LEFT JOIN client_audiences ca on audiences.id = ca.audience_id WHERE ca.client_id = ?";
                    List<String> aud = jdbcTemplate.queryForList(query, String.class, rc.getId());
                    claim.put("client_id", rc.getClientId());
                    claim.put("aud", aud);
                    claim.put("scope", rc.getScopes());
                }
            });
        }
    }
}
