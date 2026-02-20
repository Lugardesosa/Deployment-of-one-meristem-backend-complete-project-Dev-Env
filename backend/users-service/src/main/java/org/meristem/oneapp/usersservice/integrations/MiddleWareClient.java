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
    MiddlewareResponse<CreateIndividualCustomerResponse> createCorporateCustomer(@RequestBody CreateCorporateCustomerRequest request);

    @PostExchange("/customers/customers/joint")
    MiddlewareResponse<CreateIndividualCustomerResponse> createJointCustomer(@RequestBody CreateJointCustomerRequest request);

    @PostExchange("/customers/customers/minor")
    MiddlewareResponse<CreateIndividualCustomerResponse> createMinorCustomer(@RequestBody CreateMinorCustomerRequest request);

    @GetExchange("/customers/customers/{customerId}/details")
    MiddlewareResponse<MiddlewareCustomerResponse> getCustomerDetails(@PathVariable String customerId);

    @PostExchange("/customers/customers/documents/individual")
    MiddlewareResponse<MiddlewareDocumentUploadResponse> uploadIndividualCustomerDocument(@RequestParam String customerId,
                                                                                          @RequestBody DocumentUploadRequest request);

    @GetExchange("/customers/customers/documents/individual/status")
    MiddlewareResponse<MiddlewareDocumentStatusResponse> getIndividualCustomerDocumentStatus(@RequestParam String customerId);

    @PostExchange("/customers/customers/documents/corporate")
    MiddlewareResponse<MiddlewareDocumentUploadResponse> uploadCorporateCustomerDocument(@RequestParam String customerId,
                                                                                         @RequestBody DocumentUploadRequest request);

    @PostExchange("/customers/customers/documents/joint")
    MiddlewareResponse<MiddlewareDocumentUploadResponse> uploadJointCustomerDocument(@RequestParam String customerId,
                                                                                     @RequestBody DocumentUploadRequest request);

    @PostExchange("/customers/customers/documents/minor")
    MiddlewareResponse<MiddlewareDocumentUploadResponse> uploadMinorCustomerDocument(@RequestParam String customerId,
                                                                                     @RequestBody DocumentUploadRequest request);

    @GetExchange("/customers/customers/documents/identification/{customerId}")
    MiddlewareResponse<MiddlewareDocumentBase64Response> getCustomerIdentificationDocument(@PathVariable String customerId);

    @GetExchange("/customers/customers/documents/picture/{customerId}")
    MiddlewareResponse<MiddlewareDocumentBase64Response> getCustomerPicture(@PathVariable String customerId);

    @GetExchange("/customers/customers/documents/signature/{customerId}")
    MiddlewareResponse<MiddlewareDocumentBase64Response> getCustomerSignature(@PathVariable String customerId);

    @GetExchange("/customers/customers/documents/utility-bill/{customerId}")
    MiddlewareResponse<MiddlewareDocumentBase64Response> getCustomerUtilityBill(@PathVariable String customerId);

    @GetExchange("/customers/customers/{customerId}/position")
    MiddlewareResponse<MiddlewareCustomerPositionResponse> getCustomerPosition(@PathVariable String customerId);

    @GetExchange("/customers/customers/{customerId}/position-by-module")
    MiddlewareResponse<MiddlewareCustomerPositionByModuleResponse> getCustomerPositionByModule(@PathVariable String customerId);

    @GetExchange("/customers/reference/identity-types")
    MiddlewareResponse<List<MiddlewareIdentityTypeResponse>> getCustomerIdentityTypes();

    @GetExchange("/customers/reference/titles")
    MiddlewareResponse<List<MiddlewareTitleResponse>> getCustomerTitles();

    @GetExchange("/customers/reference/states")
    MiddlewareResponse<List<MiddlewareStateResponse>> getCustomerStates();

    @GetExchange("/customers/reference/countries")
    MiddlewareResponse<List<MiddlewareCountryResponse>> getCustomerCountries();

    @GetExchange("/customers/reference/banks")
    MiddlewareResponse<List<MiddlewareBankResponse>> getCustomerBanks();

    @GetExchange("/customers/reference/currencies")
    MiddlewareResponse<MiddlewareCurrencyResponse> getCurrencies();

    @GetExchange("/customers/reference/lgas")
    MiddlewareResponse<MiddlewareLgaResponse> getLgas();

    @GetExchange("/customers/reference/locations")
    MiddlewareResponse<MiddlewareLocationResponse> getLocations();

    @GetExchange("/customers/reference/nationalities")
    MiddlewareResponse<MiddlewareNationalityResponse> getNationalities();

    @GetExchange("/customers/reference/officers")
    MiddlewareResponse<MiddlewareOfficerResponse> getOfficers();

    @GetExchange("/customers/reference/positions")
    MiddlewareResponse<MiddlewarePositionResponse> getPositions();

    @GetExchange("/customers/reference/relationships")
    MiddlewareResponse<MiddlewareRelationResponse> getRelationships();

    @GetExchange("/customers/reference/sectors")
    MiddlewareResponse<MiddlewareSectorResponse> getSectors();

    @GetExchange("/customers/reference/turnovers")
    MiddlewareResponse<MiddlewareTurnoverResponse> getTurnovers();

    @PostExchange("/customers/customers/{customerId}/update/idn")
    MiddlewareResponse<MiddlewareBaseApiResponse> updateIndividualCustomerIdn(@PathVariable String customerId,
                                                                              @RequestBody UpdateIdnRequest request);

    @PostExchange("/customers/customers/{customerId}/update/address")
    MiddlewareResponse<MiddlewareBaseApiResponse> updateIndividualCustomerAddress(@PathVariable String customerId,
                                                                                  @RequestBody UpdateAddressRequest request);

    @PostExchange("/customers/customers/{customerId}/update/email")
    MiddlewareResponse<MiddlewareBaseApiResponse> updateIndividualCustomerEmail(@PathVariable String customerId,
                                                                                @RequestBody UpdateEmailRequest request);

    @PostExchange("/customers/customers/{customerId}/update/employment")
    MiddlewareResponse<MiddlewareBaseApiResponse> updateIndividualCustomerEmployment(@PathVariable String customerId,
                                                                                     @RequestBody UpdateEmploymentRequest request);

    @PostExchange("/customers/customers/{customerId}/update/employment/remove")
    MiddlewareResponse<MiddlewareBaseApiResponse> removeIndividualEmployment(@PathVariable String customerId);

    @GetExchange("/customers/health")
    Object health();

    @GetExchange("/customers/customers/by-id/{customerId}")
    MiddlewareResponse<MiddlewareCustomerResponse> getCustomerById(@PathVariable String customerId);

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
    MiddlewareResponse<MiddlewareBaseApiResponse> upgradeMinor(@RequestBody MinorUpgradeRequest request);
}
