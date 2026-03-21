package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.requests.BeneficiaryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BeneficiaryResponse;

import java.util.List;

/**
 * Interface for managing beneficiary-related operations.
 * Provides functionality for creating, retrieving, and managing beneficiaries.
 */
public interface IBeneficiaryService {

    /**
     * Creates a new beneficiary record for the currently authenticated owner.
     *
     * @param request the beneficiary creation payload
     * @return a representation of the created beneficiary
     */
    BeneficiaryResponse createBeneficiary(BeneficiaryRequest request);

    /**
     * Retrieves a beneficiary by ID for a specific user.
     *
     * @param beneficiaryId the ID of the beneficiary
     * @param userId the ID of the user
     * @return a {@link BeneficiaryResponse} containing the beneficiary details
     */
    BeneficiaryResponse getBeneficiary(Long beneficiaryId, Long userId);

    /**
     * Retrieves all beneficiaries for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link BeneficiaryResponse} containing beneficiaries
     */
    List<BeneficiaryResponse> getBeneficiaries(Long userId);
}
