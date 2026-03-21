package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.requests.RiskAppetiteRequest;
import org.meristem.oneapp.usersservice.domains.responses.RiskAppetiteResponse;

/**
 * Interface for managing risk appetite operations.
 * Provides functionality for determining and storing user risk profiles.
 */
public interface IRiskAppetiteService {

    /**
     * Derives and persists the logged-in user's risk appetite based on the provided questionnaire inputs.
     *
     * @param request the user's questionnaire responses used to determine risk appetite
     * @return a response containing the derived risk appetite, risk profile, and description lines
     */
    RiskAppetiteResponse createOrUpdateRiskAppetite(RiskAppetiteRequest request);
}
