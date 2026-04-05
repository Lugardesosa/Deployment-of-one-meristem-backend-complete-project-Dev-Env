package org.meristem.oneapp.wealthservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.constants.ApiConstants;
import org.meristem.oneapp.wealthservice.domains.responses.AppResponse;
import org.meristem.oneapp.wealthservice.domains.responses.InvestmentSummaryResponse;
import org.meristem.oneapp.wealthservice.services.IInvestmentSummaryService;
import org.meristem.oneapp.wealthservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "investments")
@Tag(name = "Investments", description = "Endpoints for all investments summary")
public class InvestmentSummaryController {

    private final IInvestmentSummaryService investmentSummaryService;

    @Operation(summary = "Get all investments", description = "Returns all investments for the logged in customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Investments returned successfully")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<InvestmentSummaryResponse>>> getAllInvestments() {
        return ApiUtil.buildResponse(investmentSummaryService.getAllInvestments(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get investment by placementId", description = "Returns a single investment by placementId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Investment returned successfully"),
            @ApiResponse(responseCode = "404", description = "Investment not found")
    })
    @GetMapping(value = "/{placementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<InvestmentSummaryResponse>> getInvestmentByPlacementId(@PathVariable String placementId) {
        return ApiUtil.buildResponse(investmentSummaryService.getInvestmentByPlacementId(placementId), HttpStatus.OK.toString(), "Successful");
    }
}