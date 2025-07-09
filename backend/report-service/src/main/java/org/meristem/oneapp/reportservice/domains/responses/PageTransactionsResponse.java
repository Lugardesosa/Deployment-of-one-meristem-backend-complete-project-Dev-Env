package org.meristem.oneapp.reportservice.domains.responses;

import lombok.Builder;

import java.util.List;

@Builder
public record PageTransactionsResponse(String date, List<TransactionsResponse> data) {
}
