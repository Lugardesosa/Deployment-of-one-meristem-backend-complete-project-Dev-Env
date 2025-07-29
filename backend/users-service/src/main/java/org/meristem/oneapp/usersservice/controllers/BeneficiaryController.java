package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.BeneficiaryRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.BeneficiaryResponse;
import org.meristem.oneapp.usersservice.services.BeneficiaryService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "beneficiary")
@Tag(name = "Beneficiary api", description = "This api controls everything beneficiary")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @Operation(summary = "Get a beneficiary")
    @ApiResponses(value = {
            @ApiResponse(description = "Returns the details of a beneficiary", responseCode = "200")
    })
    @PreAuthorize("hasRole('ROLE_user.beneficiary.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<BeneficiaryResponse>> getBeneficiary(@RequestParam(name = "email")  String email) {
        return ApiUtil.buildResponse(beneficiaryService.getBeneficiary(email), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get beneficiaries")
    @ApiResponses(value = {
            @ApiResponse(description = "Returns the details of all beneficiaries", responseCode = "200")
    })
    @PreAuthorize("hasRole('ROLE_user.beneficiary.get')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<BeneficiaryResponse>>> getBeneficiaries() {
        return ApiUtil.buildResponse(beneficiaryService.getBeneficiaries(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Create a beneficiary")
    @ApiResponses(value = {
            @ApiResponse(description = "Create a beneficiary", responseCode = "201")
    })
    @PreAuthorize("hasRole('ROLE_user.beneficiary.create')")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<BeneficiaryResponse>> createBeneficiary(@Valid @RequestBody BeneficiaryRequest request) {
        return ApiUtil.buildResponse(beneficiaryService.createBeneficiary(request), HttpStatus.CREATED.toString(), "Successful");
    }
}
