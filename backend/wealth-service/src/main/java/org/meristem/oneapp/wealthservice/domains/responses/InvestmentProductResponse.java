package org.meristem.oneapp.wealthservice.domains.responses;

public record InvestmentProductResponse(String productId,
                                        String name,
                                        String category,
                                        String currency) {
}
