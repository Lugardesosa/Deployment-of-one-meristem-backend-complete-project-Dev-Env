package org.meristem.oneapp.usersservice.integrations;

import org.meristem.oneapp.usersservice.integrations.requests.*;
import org.meristem.oneapp.usersservice.integrations.responses.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface MiddleWareClient {

    @PostExchange("/customers/customers/individual")
    MiddlewareResponse<CreateIndividualCustomerResponse> createIndividualCustomer(@RequestBody CreateIndividualCustomerRequest request);

    @PostExchange("/customers/customers/corporate")
    CreateIndividualCustomerResponse createCorporateCustomer(@RequestBody CreateCorporateCustomerRequest request);

    @PostExchange("/customers/customers/joint")
    CreateIndividualCustomerResponse createJointCustomer(@RequestBody CreateJointCustomerRequest request);

    @PostExchange("/customers/customers/minor")
    CreateIndividualCustomerResponse createMinorCustomer(@RequestBody CreateMinorCustomerRequest request);

    @GetExchange("/customers/customers/{customerId}/details")
    MiddlewareCustomerResponse getCustomerDetails(@PathVariable String customerId);

    @PostExchange("/customers/customers/documents/individual")
    MiddlewareDocumentUploadResponse uploadIndividualCustomerDocument(@RequestParam String customerId,
                                                                      @RequestBody DocumentUploadRequest request);

    @GetExchange("/customers/customers/documents/individual/status")
    MiddlewareDocumentStatusResponse getIndividualCustomerDocumentStatus(@RequestParam String customerId);

    @PostExchange("/customers/customers/documents/corporate")
    MiddlewareDocumentUploadResponse uploadCorporateCustomerDocument(@RequestParam String customerId,
                                                                     @RequestBody DocumentUploadRequest request);

    @PostExchange("/customers/customers/documents/joint")
    MiddlewareDocumentUploadResponse uploadJointCustomerDocument(@RequestParam String customerId,
                                                                 @RequestBody DocumentUploadRequest request);

    @PostExchange("/customers/customers/documents/minor")
    MiddlewareDocumentUploadResponse uploadMinorCustomerDocument(@RequestParam String customerId,
                                                                 @RequestBody DocumentUploadRequest request);

    @GetExchange("/customers/customers/documents/identification/{customerId}")
    MiddlewareDocumentBase64Response getCustomerIdentificationDocument(@PathVariable String customerId);

    @GetExchange("/customers/customers/documents/picture/{customerId}")
    MiddlewareDocumentBase64Response getCustomerPicture(@PathVariable String customerId);

    @GetExchange("/customers/customers/documents/signature/{customerId}")
    MiddlewareDocumentBase64Response getCustomerSignature(@PathVariable String customerId);

    @GetExchange("/customers/customers/documents/utility-bill/{customerId}")
    MiddlewareDocumentBase64Response getCustomerUtilityBill(@PathVariable String customerId);

    @GetExchange("/customers/customers/{customerId}/position")
    MiddlewareCustomerPositionResponse getCustomerPosition(@PathVariable String customerId);

    @GetExchange("/customers/customers/{customerId}/position-by-module")
    MiddlewareCustomerPositionByModuleResponse getCustomerPositionByModule(@PathVariable String customerId);

    @GetExchange("/customers/reference/identity-types")
    List<MiddlewareIdentityTypeResponse> getCustomerIdentityTypes();

    @GetExchange("/customers/reference/titles")
    List<MiddlewareTitleResponse> getCustomerTitles();

    @GetExchange("/customers/reference/states")
    List<MiddlewareStateResponse> getCustomerStates();

    @GetExchange("/customers/reference/countries")
    List<MiddlewareCountryResponse> getCustomerCountries();

    @GetExchange("/customers/reference/banks")
    List<MiddlewareBankResponse> getCustomerBanks();

    @GetExchange("/customers/reference/currencies")
    MiddlewareCurrencyResponse getCurrencies();

    @GetExchange("/customers/reference/lgas")
    MiddlewareLgaResponse getLgas();

    @GetExchange("/customers/reference/locations")
    MiddlewareLocationResponse getLocations();

    @GetExchange("/customers/reference/nationalities")
    MiddlewareNationalityResponse getNationalities();

    @GetExchange("/customers/reference/officers")
    MiddlewareOfficerResponse getOfficers();

    @GetExchange("/customers/reference/positions")
    MiddlewarePositionResponse getPositions();

    @GetExchange("/customers/reference/relationships")
    MiddlewareRelationResponse getRelationships();

    @GetExchange("/customers/reference/sectors")
    MiddlewareSectorResponse getSectors();

    @GetExchange("/customers/reference/turnovers")
    MiddlewareTurnoverResponse getTurnovers();

    @PostExchange("/customers/customers/update/idn")
    MiddlewareBaseApiResponse updateIndividualCustomerIdn(@RequestBody UpdateIdnRequest request);

    @PostExchange("/customers/customers/update/address")
    MiddlewareResponse<MiddlewareBaseApiResponse> updateIndividualCustomerAddress(@RequestBody UpdateAddressRequest request);

    @PostExchange("/customers/customers/update/email")
    MiddlewareBaseApiResponse updateIndividualCustomerEmail(@RequestBody UpdateEmailRequest request);

    @PostExchange("/customers/customers/update/employment")
    MiddlewareBaseApiResponse updateIndividualCustomerEmployment(@RequestBody UpdateEmploymentRequest request);

    @PostExchange("/customers/customers/update/employment/remove")
    MiddlewareBaseApiResponse removeIndividualEmployment(@RequestParam String customerId);

    @GetExchange("/customers/customers/by-id/{customerId}")
    MiddlewareCustomerResponse getCustomerById(@PathVariable String customerId);

    @GetExchange("/customers/customers/by-bvn/{bvn}")
    MiddlewareResponse<MiddlewareCustomerResponse> getCustomerByBvn(@PathVariable String bvn);

    @GetExchange("/customers/customers/by-nin/{nin}")
    MiddlewareResponse<MiddlewareCustomerResponse> getCustomerByNin(@PathVariable String nin);

    @GetExchange("/customers/customers/by-email/{email}")
    MiddlewareResponse<MiddlewareCustomerResponse> getCustomerByEmail(@PathVariable String email);

    @GetExchange("/customers/customers/by-phone/{phone}")
    MiddlewareResponse<MiddlewareCustomerResponse> getCustomerByPhone(@PathVariable String phone);

    @GetExchange("/customers/customers/minors")
    MiddlewareResponse<List<MiddlewareCustomerResponse>> getMinorCustomers();

    @GetExchange("/customers/customers/by-name/{customerName}")
    MiddlewareResponse<List<MiddlewareCustomerResponse>> getCustomerByName(@PathVariable String customerName);

    @GetExchange("/customers/customers/minors/by-parent/{parentCustomerId}")
    MiddlewareResponse<List<MiddlewareCustomerResponse>> getMinorsByParentId(@PathVariable String parentCustomerId);

    @PostExchange("/customers/customers/minors/upgrade")
    MiddlewareBaseApiResponse upgradeMinor(@RequestBody MinorUpgradeRequest request);
}
