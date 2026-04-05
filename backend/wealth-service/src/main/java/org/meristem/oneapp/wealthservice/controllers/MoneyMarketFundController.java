package org.meristem.oneapp.wealthservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.constants.ApiConstants;
import org.meristem.oneapp.wealthservice.domains.requests.MoneyMarketFundPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.requests.MoneyMarketFundSubscribeRequest;
import org.meristem.oneapp.wealthservice.domains.responses.AppResponse;
import org.meristem.oneapp.wealthservice.domains.responses.MoneyMarketFundPreviewResponse;
import org.meristem.oneapp.wealthservice.domains.responses.MoneyMarketFundSubscribeResponse;
import org.meristem.oneapp.wealthservice.services.IMoneyMarketFundService;
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
@RequestMapping(ApiConstants.CONTEXT_PATH + "money-market-fund")
@Tag(name = "Money Market Fund", description = "Endpoints for money market fund operations")
public class MoneyMarketFundController {

    private final IMoneyMarketFundService moneyMarketFundService;

    @Operation(summary = "Preview money market fund", description = "Preview investment details for a money market fund")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preview returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<MoneyMarketFundPreviewResponse>> preview(@Valid @RequestBody MoneyMarketFundPreviewRequest request) {
        return ApiUtil.buildResponse(moneyMarketFundService.preview(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Subscribe to money market fund", description = "Subscribe to a money market fund")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subscription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping(value = "/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<MoneyMarketFundSubscribeResponse>> subscribe(@Valid @RequestBody MoneyMarketFundSubscribeRequest request) {
        return ApiUtil.buildResponse(moneyMarketFundService.subscribe(request), HttpStatus.OK.toString(), "Successful");
    }



}