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
import org.meristem.oneapp.coreservices.wealth.domains.requests.PortfolioStatementRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.AppResponse;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioActivityResponse;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioAssetClassBreakdownResponse;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioStatementResponse;
import org.meristem.oneapp.coreservices.wealth.services.IPortfolioService;
import org.meristem.oneapp.coreservices.wealth.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "portfolio")
@Tag(name = "Wealth Portfolio", description = "Endpoints for portfolio overview and statement operations")
public class PortfolioController {

    private final IPortfolioService portfolioService;

    @Operation(summary = "Get recent portfolio activity", description = "Returns recent activity for a customer portfolio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recent activity returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customerId")
    })
    @GetMapping(value = "/activity/recent/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<PortfolioActivityResponse>>> getRecentPortfolioActivity(@PathVariable String customerId) {
        return ApiUtil.buildResponse(portfolioService.getRecentPortfolioActivity(customerId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get portfolio breakdown", description = "Returns portfolio asset class breakdown")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Breakdown returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customerId")
    })
    @GetMapping(value = "/breakdown/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<PortfolioAssetClassBreakdownResponse>>> getPortfolioBreakdown(@PathVariable String customerId) {
        return ApiUtil.buildResponse(portfolioService.getPortfolioBreakdown(customerId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Request portfolio statement", description = "Creates a portfolio statement generation request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statement request created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PortfolioStatementRequest.class)))
    @PostMapping(value = "/statement/request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PortfolioStatementResponse>> requestPortfolioStatement(@Valid @RequestBody PortfolioStatementRequest request) {
        return ApiUtil.buildResponse(portfolioService.requestPortfolioStatement(request), HttpStatus.OK.toString(), "Successful");
    }
}
