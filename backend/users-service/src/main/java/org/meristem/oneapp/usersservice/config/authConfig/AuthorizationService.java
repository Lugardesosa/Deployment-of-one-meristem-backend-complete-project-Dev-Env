package org.meristem.oneapp.usersservice.config.authConfig;


import org.meristem.oneapp.usersservice.domains.enums.UserStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("authz")
public class AuthorizationService {

    public Boolean hasStatus(Authentication authentication, UserStatus status) {

        if (authentication instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            Long status1 = jwt.getClaim("status");
            return status.getValue().equals(status1.intValue());
        }
        return false;
    }
}
