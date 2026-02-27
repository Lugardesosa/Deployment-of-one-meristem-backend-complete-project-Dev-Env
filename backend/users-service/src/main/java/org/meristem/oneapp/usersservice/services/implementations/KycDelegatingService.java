package org.meristem.oneapp.usersservice.services.implementations;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.IdValidationResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.services.IKycDelegatingService;
import org.meristem.oneapp.usersservice.services.IKycService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


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

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public KycDelegatingService(@Qualifier("SMILE_ID") IKycService smileIdService, @Qualifier("DOJAH") IKycService dojahService) {
        this.smileIdService = smileIdService;
        this.dojahService = dojahService;
    }

    @Override
    @CircuitBreaker(name = "dojah", fallbackMethod = "smileIdBvnQuery")
    public BvnQueryResponse bvnQuery(IdQueryRequest request) {
        return dojahService.bvnQuery(request);
    }

    public BvnQueryResponse smileIdBvnQuery(IdQueryRequest request, Throwable throwable) {

        if (throwable instanceof ResourceNotFoundException ex && "prod".equalsIgnoreCase(activeProfile)) {
            throw new ResourceNotFoundException("ID query not found", request.idType(), request.idNumber());
        }
        return smileIdService.bvnQuery(request);
    }

    @Override
    @CircuitBreaker(name = "dojah", fallbackMethod = "smileIdValidateNin")
    public IdValidationResponse validateNin(IdQueryRequest request) {
        return this.dojahService.validateNin(request);
    }

    public IdValidationResponse smileIdValidateNin(IdQueryRequest request, Throwable throwable) {
        if (throwable instanceof ResourceNotFoundException ex && "prod".equalsIgnoreCase(activeProfile)) {
            throw new ResourceNotFoundException("ID query not found", request.idType(), request.idNumber());
        }
        return this.smileIdService.validateNin(request);
    }
    
    @Override
    public IdValidationResponse validateBvn(MultipartFile file) {
        return dojahService.bvnValidation(file);
    }
}
