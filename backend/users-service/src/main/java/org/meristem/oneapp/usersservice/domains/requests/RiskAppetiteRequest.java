package org.meristem.oneapp.usersservice.domains.requests;

import com.ecwid.consul.v1.agent.AgentClient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.domains.enums.*;

import java.util.Set;

public record RiskAppetiteRequest(@NotNull(message = "Cannot be null") @Schema(description = "Age bracket of the user", example = "EIGHTEEN_34", anyOf = {AgeBracket.class}) AgeBracket ageBracket,
                                  @NotNull(message = "Cannot be null") @Schema(description = "Source of income of the user", example = "[PROCEEDS_FROM_INVESTMENT, ALLOWANCES_BONUSES]", anyOf = {SourceOfIncome.class}) Set<SourceOfIncome> sourceOfIncome,
                                  @NotNull(message = "Cannot be null") @Schema(description = "Annual income of the user", example = "LESS_THAN_1_MILLION", anyOf = {AnnualIncome.class}) AnnualIncome annualIncome,
                                  @NotNull(message = "Cannot be null") @Schema(description = "Investment duration of the user", example = "ZERO_12_MONTH", anyOf = {InvestmentDuration.class}) InvestmentDuration investmentDuration,
                                  @NotNull(message = "Cannot be null") @Schema(description = "Investment time of the user", example = "LESS_THAN_1_YEAR", anyOf = {InvestmentTime.class}) InvestmentTime investmentTime,
                                  @NotNull(message = "Cannot be null") @Schema(description = "Investment objective of the user", example = "GROWTH", anyOf = {InvestmentObjective.class}) InvestmentObjective investmentObjective) {
}
