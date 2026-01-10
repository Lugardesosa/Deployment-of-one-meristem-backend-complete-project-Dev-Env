package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.requests.CreateNextOfKinRequest;
import org.meristem.oneapp.usersservice.domains.responses.NextOfKinResponse;

/**
 * Interface for managing next-of-kin-related operations.
 * Provides functionality for creating and managing next-of-kin details for users.
 *
 * @author realninety5
 */
public interface INextOfKinService {

    /**
     * Creates a new next-of-kin entry for the currently logged-in user.
     *
     * @param createNextOfKinRequest the request containing next-of-kin details
     * @return a {@link NextOfKinResponse} containing the created next-of-kin details
     */
    NextOfKinResponse createNextOfKin(CreateNextOfKinRequest createNextOfKinRequest);

    /**
     * Retrieves the next-of-kin details for the currently logged-in user.
     *
     * @return a {@link NextOfKinResponse} containing the next-of-kin details
     */
    NextOfKinResponse getNextOfKin();
}
