package org.meristem.oneapp.coreservices.notifications.domains.responses;

import java.math.BigDecimal;

public record TransactionTransferResponse(BigDecimal amount, String customerId, String currency) {
}
