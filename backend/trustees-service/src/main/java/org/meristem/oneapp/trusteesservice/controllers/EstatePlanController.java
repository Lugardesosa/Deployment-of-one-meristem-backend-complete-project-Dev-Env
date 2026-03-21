package org.meristem.oneapp.trusteesservice.controllers;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.trusteesservice.constants.ApiConstants;
import org.meristem.oneapp.trusteesservice.domains.enums.Plans;
import org.meristem.oneapp.trusteesservice.domains.requests.*;
import org.meristem.oneapp.trusteesservice.domains.responses.*;
import org.meristem.oneapp.trusteesservice.services.IEstatePlanService;
import org.meristem.oneapp.trusteesservice.services.implementations.EstatePlanService;
import org.meristem.oneapp.trusteesservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(ApiConstants.CONTEXT_PATH + "plans")
@RestController
@RequiredArgsConstructor
@Tag(name = "Estate Plan API", description = "Controls everything estate plans")
public class EstatePlanController {

    private final IEstatePlanService estatePlanService;


    @Operation(summary = "Create a simple will", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a simple will")})
    @PreAuthorize("hasRole('ROLE_1024') or hasRole('ROLE_2006')")
    @PostMapping(value = "/simple-will", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> saveSimpleWill(@RequestBody @Valid CreateWillRequest request) {
        return ApiUtil.buildResponse(estatePlanService.saveSimpleWill(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a comprehensive will", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a comprehensive will")})
    @PreAuthorize("hasRole('ROLE_1024') or hasRole('ROLE_2006')")
    @PostMapping(value = "/comprehensive-will", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> createComprehensiveWill(@RequestBody @Valid CreateComprehensiveWillRequest request) {
        return ApiUtil.buildResponse(estatePlanService.saveComprehensiveWill(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Add a beneficiary", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Add a beneficiary")})
    @PreAuthorize("hasRole('ROLE_users.beneficiary.add') OR hasRole('ROLE_2011')")
    @PutMapping(value = "/add-beneficiary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> addBeneficiary(@RequestBody @Valid AddBeneficiaryRequest request) {
        return ApiUtil.buildResponse(estatePlanService.addBeneficiary(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Remove a beneficiary", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Remove a beneficiary")})
    @PreAuthorize("hasRole('ROLE_users.beneficiary.remove') OR hasRole('ROLE_2013')")
    @PutMapping(value = "/remove-beneficiary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> removeBeneficiary(@RequestBody @Valid RemoveBeneficiaryRequest request) {
        return ApiUtil.buildResponse(estatePlanService.removeBeneficiary(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Add a asset", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Add a asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.add')")
    @PutMapping(value = "/add-asset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> addAsset(@RequestBody @Valid AddAssetRequest request) {
        return ApiUtil.buildResponse(estatePlanService.addAsset(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Remove an asset", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Remove a asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.remove')")
    @PutMapping(value = "/remove-asset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> addAsset(@RequestBody @Valid RemoveAssetRequest request) {
        return ApiUtil.buildResponse(estatePlanService.removeAsset(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Hidden
    @Operation(summary = "Add an executor", method = "PUT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Add an executor")})
    @PreAuthorize("hasRole('ROLE_users.executor.add') OR hasRole('ROLE_2007')")
    @PutMapping(value = "/add-executor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> addExecutor(@RequestBody @Valid AddExecutorRequest request) {
        return ApiUtil.buildResponse(estatePlanService.addExecutor(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a nominated fund", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a nominated fund")})
    @PreAuthorize("hasRole('ROLE_1024')")
    @PostMapping(value = "/nominated-fund", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> saveNominatedFund(@RequestBody @Valid CreateNominatedFundRequest request) {
        return ApiUtil.buildResponse(estatePlanService.saveNominatedFund(request), HttpStatus.CREATED.toString(), "Successful");
    }


    @Operation(summary = "Create a private trusts", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a private trusts")})
    @PreAuthorize("hasRole('ROLE_1024') or hasRole('ROLE_2006')")
    @PostMapping(value = "/private-trusts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EstatePlanResponse>> savePrivateTrust(@RequestBody @Valid CreatePrivateTrustsRequest request) {
        return ApiUtil.buildResponse(estatePlanService.savePrivateTrust(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Get a plan (s)", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get a plan (s)",
            content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GetPlanResponse.class)
            )})
    @PreAuthorize("hasRole('ROLE_1025') OR hasRole('ROLE_2008')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<GetPlanResponse>> getPlans(@RequestParam(name = "plan") Plans plan, @RequestParam(name = "plan-id", required = false) Long planId,
                                                                 @RequestParam(name = "ownerId", required = false) Long ownerId) {
        return ApiUtil.buildResponse(estatePlanService.getPlans(plan, planId, ownerId), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Get all plans", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get all plans")
    @PreAuthorize("hasRole('ROLE_1025') OR hasRole('ROLE_2008')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Map<String, List<?>>>> getAllAssets(@RequestParam(name = "ownerId", required = false) Long ownerId) {
        return ApiUtil.buildResponse(estatePlanService.getAllPlans(ownerId), HttpStatus.OK.toString(), "Successful");
    }


    @Operation(summary = "Get total assets value", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get total assets value",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GetAssetValueResponse.class)
            )})
    @PreAuthorize("hasRole('ROLE_1025') OR hasRole('ROLE_2043')")
    @GetMapping(value = "/beneficiary-plans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<GetBeneficiaryPlansResponse>> getBeneficiaryValue(@RequestParam(name = "beneficiary-id") Long beneficiaryId, @RequestParam(name = "ownerId", required = false) Long ownerId) {
        return ApiUtil.buildResponse(estatePlanService.getBeneficiaryValue(beneficiaryId, ownerId), HttpStatus.OK.toString(), "Successful");
    }
}
