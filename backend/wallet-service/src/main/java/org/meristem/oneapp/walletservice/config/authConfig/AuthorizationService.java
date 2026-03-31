package org.meristem.oneapp.walletservice.config.authConfig;


import jakarta.servlet.http.HttpServletRequest;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("authz")
public class AuthorizationService {

    private final HttpServletRequest request;

    public AuthorizationService(HttpServletRequest request) {
        this.request = request;
    }

    public Boolean ownsCustomerId() {
        List<String> customerIds = AppUtil.getCustomerId();
        return customerIds.contains(AppUtil.getCustomerId(request));
    }
}
