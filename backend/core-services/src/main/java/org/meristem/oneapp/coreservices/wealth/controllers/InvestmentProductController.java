package org.meristem.oneapp.coreservices.wealth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.wealth.constants.ApiConstants;
import org.meristem.oneapp.coreservices.wealth.domains.responses.AppResponse;
import org.meristem.oneapp.coreservices.wealth.domains.responses.InvestmentProductResponse;
import org.meristem.oneapp.coreservices.wealth.services.IInvestmentProductService;
import org.meristem.oneapp.coreservices.wealth.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "products")
@Tag(name = "Wealth Products", description = "Endpoints for investment product catalog")
public class InvestmentProductController {

    private final IInvestmentProductService investmentProductService;

    @Operation(summary = "List investment products", description = "Returns all available investment products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products returned successfully")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<InvestmentProductResponse>>> getInvestmentProducts() {
        return ApiUtil.buildResponse(investmentProductService.getInvestmentProducts(), HttpStatus.OK.toString(), "Successful");
    }
}
