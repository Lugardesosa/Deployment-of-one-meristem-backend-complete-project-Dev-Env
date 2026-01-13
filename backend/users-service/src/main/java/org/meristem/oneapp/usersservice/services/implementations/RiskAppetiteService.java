package org.meristem.oneapp.usersservice.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.AgeBracket;
import org.meristem.oneapp.usersservice.domains.enums.InvestmentDuration;
import org.meristem.oneapp.usersservice.domains.enums.InvestmentObjective;
import org.meristem.oneapp.usersservice.domains.enums.InvestmentTime;
import org.meristem.oneapp.usersservice.domains.requests.RiskAppetiteRequest;
import org.meristem.oneapp.usersservice.domains.responses.RiskAppetiteResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.RiskAppetite;
import org.meristem.oneapp.usersservice.repositories.CustomRepository;
import org.meristem.oneapp.usersservice.services.IRiskAppetiteService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.meristem.oneapp.usersservice.domains.enums.RiskAppetite.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RiskAppetiteService implements IRiskAppetiteService {

    private final CustomRepository customRepository;

    /**
     * Derives and persists the logged-in user's risk appetite based on the provided questionnaire inputs.
     * The mapping uses combinations of age bracket, investment duration, time horizon, and investment objective
     * to assign a predefined risk profile, stores it, and returns a summary response.
     *
     * @param request the user's questionnaire responses used to determine risk appetite
     * @return a response containing the derived risk appetite, risk profile, and description lines
     * @throws BadRequestException if the provided inputs do not correspond to a supported risk profile
     */
    public RiskAppetiteResponse createOrUpdateRiskAppetite(RiskAppetiteRequest request) {

        Long loggedInUserId = AppUtil.getLoggedInUserId();
        RiskAppetite riskAppetite;
        if (List.of(InvestmentDuration.ZERO_12_MONTH, InvestmentDuration.ONE_TO_FIVE_YEARS).contains(request.investmentDuration())
        && List.of(InvestmentTime.LESS_THAN_1_YEAR, InvestmentTime.BETWEEN_1_3_YEARS).contains(request.investmentTime())
        && List.of(InvestmentObjective.GROWTH, InvestmentObjective.QUICK_RETURNS).contains(request.investmentObjective())) {

            riskAppetite = RiskAppetite.builder().riskType(CONSERVATIVE.getRiskType()).userId(loggedInUserId)
                    .riskAppetiteDescription(CONSERVATIVE.getDescription()).riskProfile(CONSERVATIVE.getRiskProfile()).build();
            customRepository.save(riskAppetite);
        }

        else if (List.of(AgeBracket.EIGHTEEN_34, AgeBracket.THIRTY_FIVE_49).contains(request.ageBracket())
        && InvestmentDuration.ONE_TO_FIVE_YEARS.equals(request.investmentDuration())
        && InvestmentTime.BETWEEN_1_3_YEARS.equals(request.investmentTime())
        && InvestmentObjective.QUICK_RETURNS.equals(request.investmentObjective())) {

            riskAppetite = RiskAppetite.builder().riskType(BALANCED.getRiskType()).userId(loggedInUserId)
                    .riskAppetiteDescription(BALANCED.getDescription()).riskProfile(BALANCED.getRiskProfile()).build();
            customRepository.save(riskAppetite);
        }

        else if (List.of(AgeBracket.EIGHTEEN_34, AgeBracket.THIRTY_FIVE_49).contains(request.ageBracket())
                && List.of(InvestmentDuration.ONE_TO_FIVE_YEARS, InvestmentDuration.SIX_TO_TEN_YEARS).contains(request.investmentDuration())
                && List.of(InvestmentTime.BETWEEN_1_3_YEARS, InvestmentTime.BETWEEN_4_6_YEARS, InvestmentTime.OVER_6_YEARS).contains(request.investmentTime())
                && InvestmentObjective.LONG_TERM_GROWTH.equals(request.investmentObjective())) {

            riskAppetite = RiskAppetite.builder().riskType(AGGRESSIVE.getRiskType()).userId(loggedInUserId)
                    .riskAppetiteDescription(AGGRESSIVE.getDescription()).riskProfile(AGGRESSIVE.getRiskProfile()).build();
            customRepository.save(riskAppetite);
        } else {
            throw new BadRequestException("Invalid risk appetite");
        }
        return RiskAppetiteResponse.builder().riskAppetite(riskAppetite.getRiskType()).riskProfile(riskAppetite.getRiskProfile())
                .description(riskAppetite.getRiskAppetiteDescription().split(System.lineSeparator())).build();
    }
}
