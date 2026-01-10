package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.requests.BvnQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.SmileIdIdRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookResponse;
import org.meristem.oneapp.usersservice.domains.responses.UpdateResponse;

/**
 * Interface for handling Smile ID-related operations.
 * Provides functionality for BVN queries, smart link generation, and webhook processing.
 */
public interface ISmileIdService {

    /**
     * Performs a BVN query through Smile ID's enhanced KYC service.
     *
     * @param request the BVN query request
     * @return a {@link BvnQueryResponse} containing user details
     */
    BvnQueryResponse bvnQuery(BvnQueryRequest request);

    /**
     * Generates a Smile ID smart link for user verification.
     *
     * @param smileRequest the request containing job ID and requirement id for verification
     * @return an {@link UpdateResponse} containing a successful message
     */
    UpdateResponse saveSmileIdTask(SmileIdIdRequest smileRequest);

    /**
     * Handles Smile ID webhook notifications for verification results.
     *
     * @param request the webhook notification containing verification details
     * @return a {@link SmileIdWebhookResponse} indicating the success or failure of the operation
     */
    SmileIdWebhookResponse handleWebhook(SmileIdWebhookNotification request);
}
