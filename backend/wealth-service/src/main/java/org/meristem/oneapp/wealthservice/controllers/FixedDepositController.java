package org.meristem.oneapp.wealthservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.constants.ApiConstants;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositCreateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositLiquidateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.*;
import org.meristem.oneapp.wealthservice.services.IFixedDepositService;
import org.meristem.oneapp.wealthservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<AppResponse<FixedDepositPreviewResponse>> preview(@RequestBody FixedDepositPreviewRequest request) {
        return ApiUtil.buildResponse(fixedDepositService.preview(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Create fixed deposit", description = "Creates a new fixed deposit placement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fixed deposit created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<FixedDepositCreateResponse>> create(@RequestBody FixedDepositCreateRequest request) {
        return ApiUtil.buildResponse(fixedDepositService.create(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get fixed deposits", description = "Returns all fixed deposits for the logged in customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fixed deposits returned successfully")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FixedDepositResponse>>> getFixedDeposits() {
        return ApiUtil.buildResponse(fixedDepositService.getFixedDeposits(), HttpStatus.OK.toString(), "Successful");
    }
    @Operation(summary = "Get fixed deposit transactions", description = "Returns transactions for a fixed deposit account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid placementId")
    })

    @GetMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FixedDepositTransactionResponse>>> getTransactions(@RequestParam String placementId) {
        return ApiUtil.buildResponse(fixedDepositService.getTransactions(placementId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Liquidate fixed deposit", description = "Liquidates a fixed deposit placement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fixed deposit liquidated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping(value = "/liquidate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<FixedDepositLiquidateResponse>> liquidate(@Valid @RequestBody FixedDepositLiquidateRequest request) {
        return ApiUtil.buildResponse(fixedDepositService.liquidate(request), HttpStatus.OK.toString(), "Successful");
    }

}