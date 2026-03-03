package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.AddressVerificationRequest;
import org.meristem.oneapp.usersservice.domains.requests.AddressVerificationStartedRequest;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.IdVerificationRequest;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.services.IKycDelegatingService;
import org.meristem.oneapp.usersservice.services.IOnboardingService;
import org.meristem.oneapp.usersservice.services.implementations.SmileIdService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "onboard")
public class OnboardingController {

    private final IOnboardingService onboardingService;
    private final IKycDelegatingService kycDelegatingService;
    private final SmileIdService smileIdService;


    @Operation(summary = "Get the onboarding flow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the onboarding flow details for a user ")
    })
    @PreAuthorize("hasRole('ROLE_1032')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<UserOnboardingResponse>>> getOnboard() {
        return ApiUtil.buildResponse(onboardingService.getOnboardingDetails(), HttpStatus.OK.toString(), "User onboarding details request successful");
    }

    @Operation(summary = "Get smile id token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows the users to get smile id token for smile id verifications")
    })
    @PreAuthorize("hasRole('ROLE_1037')")
    @PostMapping(value = "/smile-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> saveIdTask(@RequestBody @Valid IdVerificationRequest smileRequest) {
        return ApiUtil.buildResponse(smileIdService.saveIdTask(smileRequest), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Mark address verification as started.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mark address verification as started.")
    })
    @PreAuthorize("hasRole('ROLE_1037')")
    @PostMapping(value = "/verify-address-started", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> addressVerificationStarted(@RequestBody @Valid AddressVerificationStartedRequest smileRequest) {
        return ApiUtil.buildResponse(onboardingService.addressVerificationStarted(smileRequest), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Get Countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Users to get all countries")
    })
    @PreAuthorize("hasRole('ROLE_1017')")
    @GetMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Page<CountriesResponse>>> getCountries() {
        return ApiUtil.buildResponse(onboardingService.getCountries(), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Get States")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Users to get all states for a given country")
    })
    @PreAuthorize("hasRole('ROLE_1018')")
    @GetMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Page<StatesResponse>>> getStates(@RequestParam Long countryId) {
        return ApiUtil.buildResponse(onboardingService.getStates(countryId), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Get Instruments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Users to get all the instruments")
    })
    @PreAuthorize("hasRole('ROLE_1023')")
    @GetMapping(value = "/instruments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<InstrumentResponse>>> getInstruments() {
        return ApiUtil.buildResponse(onboardingService.getInstruments(), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Submit Address for manual verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submit Address for manual verification")
    })
    @PreAuthorize("hasRole('ROLE_1012')")
    @PostMapping(value = "/submit-address", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AddressVerificationResponse>> submitAddress(@RequestBody @Valid AddressVerificationRequest request) {
        return ApiUtil.buildResponse(onboardingService.submitAddress(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Query BVN Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query BVN Details")
    })
    @PreAuthorize("hasAuthority('SCOPE_id.query') OR hasRole('ROLE_1004')")
    @PostMapping(value = "/id-query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<BvnQueryResponse>> bvnQuery(@RequestBody @Valid IdQueryRequest request) {
        return ApiUtil.buildResponse(kycDelegatingService.bvnQuery(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get customer's id number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get customer's BVN or NIN")
    })
    @PreAuthorize("hasRole('ROLE_1004')")
    @GetMapping(value = "/id-number", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<GetIdNumberResponse>> getIdNumber(@Parameter(example = "BVN", description = "Pass a valid id type (BVN or NIN)") @Pattern(regexp = "^BVN|NIN$", message = "Pass a valid id type (BVN or NIN)") @RequestParam String idType) {
        return ApiUtil.buildResponse(onboardingService.getIdNumber(idType), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Validate NIN Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validate NIN Details")
    })
    @PreAuthorize("hasRole('ROLE_1004')")
    @PostMapping(value = "/validate-nin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<IdValidationResponse>> validateNin(@RequestBody @Valid IdQueryRequest request) {
        return ApiUtil.buildResponse(kycDelegatingService.validateNin(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Validate BVN Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validate BVN Details")
    })
    @PreAuthorize("hasRole('ROLE_1004')")
    @PostMapping(value = "/validate-bvn", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AppResponse<IdValidationResponse>> validateNin(@RequestParam("photo") MultipartFile file) {
        return ApiUtil.buildResponse(kycDelegatingService.validateBvn(file), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get occupations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get occupations")
    })
    @PreAuthorize("hasAuthority('SCOPE_get.occupation')")
    @GetMapping(value = "/occupations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<OccupationResponse>> occupations() {
        return ApiUtil.buildResponse(onboardingService.getOccupations(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get source of income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get source of income")
    })
    @PreAuthorize("hasAuthority('SCOPE_get.source_of_income')")
    @GetMapping(value = "/source_of_income", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SourceOfIncomeResponse>> sourceOfIncome() {
        return ApiUtil.buildResponse(onboardingService.getsourceOfIncome(), HttpStatus.OK.toString(), "Successful");
    }
}
