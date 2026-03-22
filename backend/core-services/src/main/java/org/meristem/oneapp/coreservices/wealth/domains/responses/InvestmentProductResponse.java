package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record InvestmentProductResponse(String productId,
                                        String name,
                                        String category,
                                        String currency) {
}
