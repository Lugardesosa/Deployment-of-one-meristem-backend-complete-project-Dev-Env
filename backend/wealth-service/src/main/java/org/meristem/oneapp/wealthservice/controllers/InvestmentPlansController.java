package org.meristem.oneapp.wealthservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.constants.ApiConstants;
import org.meristem.oneapp.wealthservice.domains.responses.AppResponse;
import org.meristem.oneapp.wealthservice.domains.responses.InvestmentProductWithPlansResponse;
import org.meristem.oneapp.wealthservice.services.IInvestmentPlansService;
import org.meristem.oneapp.wealthservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "investment-plans")
@Tag(name = "Investment Plans", description = "Endpoints for investment products and their plans")
public class InvestmentPlansController {

    private final IInvestmentPlansService investmentPlansService;

    @Operation(summary = "Get all investment products with plans", description = "Returns all investment products with their plans and settings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products with plans returned successfully")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<InvestmentProductWithPlansResponse>>> getAllProductsWithPlans() {
        return ApiUtil.buildResponse(investmentPlansService.getAllProductsWithPlans(), HttpStatus.OK.toString(), "Successful");
    }
}