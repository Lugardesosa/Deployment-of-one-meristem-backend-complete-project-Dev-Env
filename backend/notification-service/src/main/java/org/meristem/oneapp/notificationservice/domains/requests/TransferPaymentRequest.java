package org.meristem.oneapp.notificationservice.domains.requests;

import java.math.BigDecimal;

public record TransferPaymentRequest(String transRef, BigDecimal amount, String customerId, String currency) {
}
