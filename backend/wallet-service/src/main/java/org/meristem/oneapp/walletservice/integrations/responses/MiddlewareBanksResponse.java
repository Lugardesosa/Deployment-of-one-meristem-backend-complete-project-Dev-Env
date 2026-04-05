package org.meristem.oneapp.walletservice.integrations.responses;

import java.util.List;

public record MiddlewareBanksResponse(

        String bankCode,
        String bankName,
        String countryCode,
        List<String> currencies
) {
}
