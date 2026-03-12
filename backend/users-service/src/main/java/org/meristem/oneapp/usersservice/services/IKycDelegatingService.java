package org.meristem.oneapp.usersservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.IdValidationResponse;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.springframework.web.multipart.MultipartFile;

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


    IdValidationResponse validateBvn(MultipartFile file);

    IdValidationResponse validateNin(IdQueryRequest request);

    IdQueryDetailsDto ninQuery(String nin);
}
