package org.meristem.oneapp.notificationservice.domains.responses;

import java.math.BigDecimal;

public record TransactionTransferResponse(BigDecimal amount, String customerId, String currency) {
}
