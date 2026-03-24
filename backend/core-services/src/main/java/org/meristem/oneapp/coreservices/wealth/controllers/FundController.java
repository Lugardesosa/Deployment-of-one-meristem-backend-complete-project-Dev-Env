package org.meristem.oneapp.coreservices.wealth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.wealth.constants.ApiConstants;
import org.meristem.oneapp.coreservices.wealth.domains.requests.FundRedemptionRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.FundSubscriptionRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;
import org.meristem.oneapp.coreservices.wealth.services.IFundService;
import org.meristem.oneapp.coreservices.wealth.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "funds")
@Tag(name = "Wealth Funds", description = "Endpoints for fund operations")
public class FundController {

    private final IFundService fundService;

    @Operation(summary = "List funds", description = "Returns all available funds")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "funds returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FundResponse>>> getAllFunds() {
        return ApiUtil.buildResponse(fundService.getAllFundTypes(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get fund details", description = "Returns details for a fund by fundId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fund details returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid fundId")
    })
    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<FundDetailsResponse>> getFundById(@RequestParam String fundId) {
        return ApiUtil.buildResponse(fundService.getFundById(fundId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Subscribe to fund", description = "Creates a fund subscription")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subscription created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FundSubscriptionRequest.class)))
    @PostMapping(value = "/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<FundSubscriptionResponse>> subscribeToFund(@RequestParam String fundId,
                                                                                       @Valid @RequestBody FundSubscriptionRequest request) {
        return ApiUtil.buildResponse(fundService.subscribeToFund(fundId, request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Redeem fund", description = "Creates a fund redemption")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Redemption created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FundRedemptionRequest.class)))
    @PostMapping(value = "/redeem", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<FundRedemptionResponse>> redeemFund(@RequestParam String fundId,
                                                                                @Valid @RequestBody FundRedemptionRequest request) {
        return ApiUtil.buildResponse(fundService.redeemFund(fundId, request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get fund accounts", description = "Returns fund accounts for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer fund accounts returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customerId")
    })
    @GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FundAccountsResponse>>> getFundAccounts(@RequestParam String customerId) {
        return ApiUtil.buildResponse(fundService.getFundAccounts(customerId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get fund portfolio", description = "Returns fund portfolio holdings for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Portfolio returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customerId")
    })
    @GetMapping(value = "/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<PortfolioResponse>>> getFundPortfolio(@RequestParam String customerId) {
        return ApiUtil.buildResponse(fundService.getPortfolio(customerId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get fund history", description = "Returns historical pricing for a fund")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fund history returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid fundId")
    })
    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FundHistoryResponse>>> getFundHistory(@RequestParam String fundId) {
        return ApiUtil.buildResponse(fundService.getFundHistory(fundId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "List fund statements", description = "Returns available fund statements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fund statements returned successfully")
    })
    @GetMapping(value = "/statements", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FundStatementResponse>>> getFundStatements() {
        return ApiUtil.buildResponse(fundService.getFundsStatements(), HttpStatus.OK.toString(), "Successful");
    }
}
