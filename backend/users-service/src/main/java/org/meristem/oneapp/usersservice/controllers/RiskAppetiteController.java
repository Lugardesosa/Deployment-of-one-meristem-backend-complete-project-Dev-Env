package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.RiskAppetiteRequest;
import org.meristem.oneapp.usersservice.domains.requests.StateUpdateRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.RiskAppetiteResponse;
import org.meristem.oneapp.usersservice.domains.responses.UpdateResponse;
import org.meristem.oneapp.usersservice.services.RiskAppetiteService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(ApiConstants.CONTEXT_PATH + "risk-appetite")
@RestController
@RequiredArgsConstructor
@Tag(name = "Risk appetite api", description = "This controller manages everything risk appetite related")
public class RiskAppetiteController {

    private final RiskAppetiteService riskAppetiteService;

    @Operation(summary = "Create or Update risk appetite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to Create or Update risk appetite")
    })
    @PreAuthorize("hasRole('ROLE_users.risk.update')")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<RiskAppetiteResponse>> createOrUpdate(@Valid @RequestBody RiskAppetiteRequest request) {
        return ApiUtil.buildResponse(riskAppetiteService.createOrUpdateRiskAppetite(request), HttpStatus.OK.toString(), "Successful");
    }
}
