package org.meristem.oneapp.usersservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.IdVerificationRequest;
import org.meristem.oneapp.usersservice.domains.responses.*;

/**
 * Interface for handling Dojah related operations.
 * Provides functionality for BVN queries, smart link generation, and webhook processing.
 */
public interface IKycDelegatingService {

    /**
     * Performs a BVN query through Smile ID's enhanced KYC service.
     *
     * @param request the BVN query request
     * @return a {@link BvnQueryResponse} containing user details
     */
    BvnQueryResponse bvnQuery(IdQueryRequest request);

    /**
     * Generates a Smile ID smart link for user verification.
     *
     * @param smileRequest the request containing job ID and requirement id for verification
     * @return an {@link UpdateResponse} containing a successful message
     */
    UpdateResponse saveIdTask(IdVerificationRequest smileRequest);

    NinValidationResponse validateNin(@Valid IdQueryRequest request);
}
