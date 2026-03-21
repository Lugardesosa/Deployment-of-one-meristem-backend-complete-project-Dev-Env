package org.meristem.oneapp.reportservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-app.report-service")
public record ReportServiceProperties(String contextPath, String applicationName) {
}
