package org.meristem.oneapp.wealthservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.constants.ApiConstants;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.AppResponse;
import org.meristem.oneapp.wealthservice.domains.responses.PlacementPreviewResponse;
import org.meristem.oneapp.wealthservice.services.IFixedDepositService;
import org.meristem.oneapp.wealthservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "fixed-deposits")
@Tag(name = "Fixed Deposits", description = "Endpoints for fixed deposit operations")
public class FixedDepositController {

    private final IFixedDepositService fixedDepositService;

    @Operation(summary = "Preview fixed deposit", description = "Preview rate and maturity for a fixed deposit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preview returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PlacementPreviewResponse>> preview(@RequestBody FixedDepositPreviewRequest request) {
        return ApiUtil.buildResponse(fixedDepositService.preview(request), HttpStatus.OK.toString(), "Successful");
    }
}