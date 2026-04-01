package org.meristem.oneapp.usersservice.config.authConfig;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.AccountType;
import org.meristem.oneapp.usersservice.repositories.IndividualAccountRepository;
import org.meristem.oneapp.usersservice.repositories.JointAccountRepository;
import org.meristem.oneapp.usersservice.repositories.UserCustomerIdsRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class OAuth2AccessTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final RegisteredClientRepository registeredClientRepository;
    private final UserCustomerIdsRepository userCustomerIdsRepository;

    @Override
    public void customize(JwtEncodingContext context) {

        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            context.getClaims().claims(claim -> {
                Object principal = context.getPrincipal().getPrincipal();
                if (principal instanceof AuthenticatedUser users) {

                    List<String> customerIds = userCustomerIdsRepository.getAllCustomerIdsByUserId(users.getId());

                    Set<String> roles = AuthorityUtils.authorityListToSet(users.getAuthorities());
                    claim.put("roles", roles);
                    claim.put("isAdmin", roles.stream().noneMatch(role -> role.equals(AppConstants.USER_ROLE)));
                    claim.put("sub", users.getEmail());
                    claim.put("firstName", users.getFirstName());
                    claim.put("lastName", users.getLastName());
                    claim.put("middleName", users.getMiddleName());
                    claim.put("id", users.getId());
                    claim.put("customerIds", customerIds);
                    claim.put("status", users.getStatus());
                    claim.put("emailVerified", users.isEmailVerified());
                    claim.put("accountType", users.getAccountType());
                } else if (principal instanceof String) {
                    RegisteredClient rc = requireNonNull(registeredClientRepository.findByClientId((String) principal), "Client not found");
                    claim.put("client_id", rc.getClientId());
                    claim.put("scope", rc.getScopes());
                }
            });
        }
    }
}
