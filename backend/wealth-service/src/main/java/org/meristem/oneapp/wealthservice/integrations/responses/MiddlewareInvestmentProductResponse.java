package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareInvestmentProductResponse(String productId,
                                        String name,
                                        String category,
                                        String currency) {
}
