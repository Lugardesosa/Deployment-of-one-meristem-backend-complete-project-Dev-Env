package org.meristem.oneapp.coreservices.notifications.domains.requests;

import java.math.BigDecimal;

public record TransferPaymentRequest(String transRef, BigDecimal amount, String customerId, String currency) {
}
