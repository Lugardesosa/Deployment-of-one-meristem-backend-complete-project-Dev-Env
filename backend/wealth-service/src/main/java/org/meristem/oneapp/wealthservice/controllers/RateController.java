package org.meristem.oneapp.wealthservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.constants.ApiConstants;
import org.meristem.oneapp.wealthservice.domains.responses.AppResponse;
import org.meristem.oneapp.wealthservice.domains.responses.RateQuoteResponse;
import org.meristem.oneapp.wealthservice.services.IRateService;
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
@RequestMapping(ApiConstants.CONTEXT_PATH + "rates")
@Tag(name = "Wealth Rates", description = "Endpoints for market and investment rates")
public class RateController {

    private final IRateService rateService;

    @Operation(summary = "Get today rates", description = "Returns today's rate quotes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rates returned successfully")
    })
    @GetMapping(value = "/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<RateQuoteResponse>>> getTodayRates() {
        return ApiUtil.buildResponse(rateService.getTodayRates(), HttpStatus.OK.toString(), "Successful");
    }
}
