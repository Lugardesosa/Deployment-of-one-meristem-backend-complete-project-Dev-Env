package org.meristem.oneapp.wealthservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.constants.ApiConstants;
import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsCreateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsLiquidateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.AppResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsCreateResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsLiquidateResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsListResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsPreviewResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsTransactionResponse;
import org.meristem.oneapp.wealthservice.services.IEthicalEarningsService;
import org.meristem.oneapp.wealthservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "ethical-earnings")
@Tag(name = "Ethical Earnings", description = "Endpoints for ethical earnings operations")
public class EthicalEarningsController {

    private final IEthicalEarningsService ethicalEarningsService;

    @Operation(summary = "Preview ethical earnings", description = "Preview rate and maturity for an ethical earnings investment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preview returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EthicalEarningsPreviewResponse>> preview(@Valid @RequestBody EthicalEarningsPreviewRequest request) {
        return ApiUtil.buildResponse(ethicalEarningsService.preview(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Create ethical earnings", description = "Creates a new ethical earnings investment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ethical earnings created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EthicalEarningsCreateResponse>> create(@Valid @RequestBody EthicalEarningsCreateRequest request) {
        return ApiUtil.buildResponse(ethicalEarningsService.create(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get ethical earnings", description = "Returns all ethical earnings investments for the logged in customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ethical earnings returned successfully")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<EthicalEarningsListResponse>>> getEthicalEarnings() {
        return ApiUtil.buildResponse(ethicalEarningsService.getEthicalEarnings(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get ethical earnings transactions", description = "Returns transactions for an ethical earnings account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid placementId")
    })
    @GetMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<EthicalEarningsTransactionResponse>>> getTransactions(@RequestParam String placementId) {
        return ApiUtil.buildResponse(ethicalEarningsService.getTransactions(placementId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Liquidate ethical earnings", description = "Liquidates an ethical earnings investment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ethical earnings liquidated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping(value = "/liquidate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EthicalEarningsLiquidateResponse>> liquidate(@Valid @RequestBody EthicalEarningsLiquidateRequest request) {
        return ApiUtil.buildResponse(ethicalEarningsService.liquidate(request), HttpStatus.OK.toString(), "Successful");
    }
}
