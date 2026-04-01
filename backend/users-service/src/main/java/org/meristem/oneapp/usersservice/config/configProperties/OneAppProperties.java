package org.meristem.oneapp.usersservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app")
public record OneAppProperties(

        String serverUrl,
        String webUrl,
        String email,
        String serverVersion,
        String serverAppName,
        String defaultHeaderName,
        Support support,
        Logo logo,
        UsersService usersService,
        WalletService walletService,
        WealthService wealthService,
        ReportService reportService,
        TrusteesService trusteesService,
        NotificationService notificationService,
        GatewayService gatewayService
) {
    public record Support(
            String email,
            String phone
    ) {
    }

    public record Logo(
            String greenUrl
    ) {
    }

    public record UsersService(
            String contextPath,
            String applicationName
    ) {
    }

    public record WalletService(
            String contextPath,
            String applicationName
    ) {
    }

    public record WealthService(
            String contextPath,
            String applicationName
    ) {
    }

    public record ReportService(
            String contextPath,
            String applicationName
    ) {
    }

    public record TrusteesService(
            String contextPath,
            String applicationName
    ) {
    }

    public record NotificationService(
            String contextPath,
            String applicationName
    ) {
    }

    public record GatewayService(
            String contextPath,
            String applicationName
    ) {
    }
}
