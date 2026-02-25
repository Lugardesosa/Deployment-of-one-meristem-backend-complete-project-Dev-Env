package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.requests.AddressVerificationRequest;
import org.meristem.oneapp.usersservice.domains.requests.AddressVerificationStartedRequest;
import org.meristem.oneapp.usersservice.domains.requests.OkHiWebhookRequest;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface for managing onboarding-related operations.
 * Provides functionality for user onboarding, address verification, and country/state lookups.
 */
public interface IOnboardingService {

    /**
     * Retrieves the onboarding details for a user.
     *
     * @return a list of user onboarding responses
     */
    List<UserOnboardingResponse> getOnboardingDetails();

    /**
     * Handles OkHi webhook callbacks and updates the user's address and onboarding status.
     *
     * @param request the webhook payload received from OkHi
     * @return a response indicating the processing outcome
     */
    WebhookResponse handleOkhiWebhook(OkHiWebhookRequest request);

    /**
     * Retrieves a paginated list of supported countries.
     *
     * @return a page of country responses
     */
    Page<CountriesResponse> getCountries();

    /**
     * Retrieves a paginated list of supported states for the default country.
     *
     * @return a page of state/province responses
     */
    Page<StatesResponse> getStates(Long countryId);

    /**
     * Retrieves all available investment instruments.
     *
     * @return a list of instrument responses
     */
    List<InstrumentResponse> getInstruments();

    /**
     * Submits address verification details.
     *
     * @param request the address verification request
     * @return an {@link AddressVerificationResponse} indicating the submission result
     */
    AddressVerificationResponse submitAddress(AddressVerificationRequest request);

    /**
     * Retrieves the ID number for a specific ID type.
     *
     * @param idType the type of ID
     * @return a {@link GetIdNumberResponse} containing the ID number
     */
    GetIdNumberResponse getIdNumber(String idType);

    UpdateResponse addressVerificationStarted(AddressVerificationStartedRequest smileRequest);

    OccupationResponse getOccupations();

    SourceOfIncomeResponse getsourceOfIncome();
}
