package org.meristem.oneapp.wealthservice.domains.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MoneyMarketFundPreviewResponse(
        String fundId,
        Double amount,
        LocalDate startDate,
        Double processingFee,
        String riskLevel,
        String returnsType,
        BigDecimal effectiveYield,
        BigDecimal grossYield,
        Long minimumInvestment,
        String investmentDenomination
) {}