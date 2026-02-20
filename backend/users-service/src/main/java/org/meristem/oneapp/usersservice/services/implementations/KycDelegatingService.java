package org.meristem.oneapp.usersservice.services.implementations;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.Vendor;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.IdVerificationRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.NinValidationResponse;
import org.meristem.oneapp.usersservice.domains.responses.UpdateResponse;
import org.meristem.oneapp.usersservice.services.IKycDelegatingService;
import org.meristem.oneapp.usersservice.services.IKycService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service class for handling Smile ID-related operations, including generating Smile ID links,
 * processing webhook notifications, and managing user onboarding requirements.
 *
 * <p>This class integrates with Smile ID's API to create smart links for user verification,
 * handle webhook notifications for verification results, and update user records accordingly.</p>
 *
 * <p>Annotated with {@link Service} to indicate that it is a Spring-managed service component.
 * Transactional methods ensure atomicity for database operations.</p>
 *
 * <p>Dependencies are injected via constructor injection, and the class uses {@link Slf4j}
 * for logging purposes.</p>
 */
@Slf4j
@Service
@Transactional
public class KycDelegatingService implements IKycDelegatingService {

    private final IKycService smileIdService;
    private final IKycService dojahService;

    public KycDelegatingService(@Qualifier("SMILE_ID") IKycService smileIdService, @Qualifier("DOJAH") IKycService dojahService) {
        this.smileIdService = smileIdService;
        this.dojahService = dojahService;
    }

    @Override
    @CircuitBreaker(name = "smileId", fallbackMethod = "dojahBvnQuery")
    public BvnQueryResponse bvnQuery(IdQueryRequest request) {
        return smileIdService.bvnQuery(request);
    }

    public BvnQueryResponse dojahBvnQuery(IdQueryRequest request, Throwable throwable) {
        return dojahService.bvnQuery(request);
    }

    @Override
    public UpdateResponse saveIdTask(IdVerificationRequest idVerificationRequest) {
        if (Vendor.SMILE_ID.compareTo(idVerificationRequest.vendor()) == 0) {
            return this.smileIdService.saveIdTask(idVerificationRequest);
        } else if (Vendor.DOJAH.compareTo(idVerificationRequest.vendor()) == 0) {
            return this.dojahService.saveIdTask(idVerificationRequest);
        } else {
            return UpdateResponse.builder().success(false).message("Wrong vendor passed").build();
        }
    }

    @Override
    @CircuitBreaker(name = "smileId", fallbackMethod = "dojahValidateNin")
    public NinValidationResponse validateNin(IdQueryRequest request) {
        return this.smileIdService.validateNin(request);
    }

    public NinValidationResponse dojahValidateNin(IdQueryRequest request, Throwable throwable) {
        return this.dojahService.validateNin(request);
    }
}
