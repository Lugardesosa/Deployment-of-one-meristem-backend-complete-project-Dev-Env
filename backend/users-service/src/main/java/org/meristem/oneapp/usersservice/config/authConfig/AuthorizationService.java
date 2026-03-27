package org.meristem.oneapp.usersservice.config.authConfig;


import jakarta.servlet.http.HttpServletRequest;
import org.meristem.oneapp.usersservice.domains.enums.UserStatus;
import org.meristem.oneapp.usersservice.repositories.UserCustomerIdsRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component("authz")
public class AuthorizationService {

    private final HttpServletRequest request;
    private final UserCustomerIdsRepository userCustomerIdsRepository;

    public AuthorizationService(HttpServletRequest request, UserCustomerIdsRepository userCustomerIdsRepository) {
        this.request = request;
        this.userCustomerIdsRepository = userCustomerIdsRepository;
    }

    public Boolean hasStatus(Authentication authentication, UserStatus status) {

        if (authentication instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            Long status1 = jwt.getClaim("status");
            return status.getValue().equals(status1.intValue());
        }
        return false;
    }


    public Boolean ownsCustomerId() {
        List<String> customerIds = AppUtil.getCustomerId();
        return customerIds.contains(AppUtil.getCustomerId(request));
    }
}
