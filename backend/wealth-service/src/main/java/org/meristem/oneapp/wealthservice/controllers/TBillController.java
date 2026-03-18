package org.meristem.oneapp.wealthservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.constants.ApiConstants;
import org.meristem.oneapp.wealthservice.domains.requests.TBillPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.requests.TBillPurchaseRequest;
import org.meristem.oneapp.wealthservice.domains.requests.TBillSellRequest;
import org.meristem.oneapp.wealthservice.domains.requests.TBillSellPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.AppResponse;
import org.meristem.oneapp.wealthservice.domains.responses.*;
import org.meristem.oneapp.wealthservice.services.ITBillService;
import org.meristem.oneapp.wealthservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "tbills")
@Tag(name = "Wealth T-Bills", description = "Endpoints for treasury bill operations")
public class TBillController {

    private final ITBillService tBillService;

    @Operation(summary = "List T-bill products", description = "Returns available T-bill products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "T-bill products returned successfully")
    })
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<TBillProductResponse>>> getTBillProducts() {
        return ApiUtil.buildResponse(tBillService.getTBillProducts(), HttpStatus.OK.toString(), "Successful");
        
    }

    @Operation(summary = "Get T-bill rate", description = "Returns rate for a T-bill instrument")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "T-bill rate returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid instrumentId")
    })
    @GetMapping(value = "/rates/{instrumentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<TBillRateResponse>> getTBillRate(@PathVariable String instrumentId) {
        return ApiUtil.buildResponse(tBillService.getTBillRate(instrumentId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Preview T-bill purchase", description = "Returns T-bill purchase preview")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Purchase preview returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TBillPreviewRequest.class)))
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<TBillPreviewResponse>> previewTBillPurchase(@Valid @RequestBody TBillPreviewRequest request) {
        return ApiUtil.buildResponse(tBillService.previewTBillPurchase(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Purchase T-bill", description = "Creates a T-bill purchase")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "T-bill purchased successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TBillPurchaseRequest.class)))
    @PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<TBillPurchaseResponse>> purchaseTBill(@Valid @RequestBody TBillPurchaseRequest request) {
        return ApiUtil.buildResponse(tBillService.purchaseTBill(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get active T-bills", description = "Returns active T-bill holdings for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active T-bills returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customerId")
    })
    @GetMapping(value = "/active/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<ActiveTBillResponse>>> getActiveTBills(@PathVariable String customerId) {
        return ApiUtil.buildResponse(tBillService.getActiveTBills(customerId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Preview T-bill sale", description = "Returns T-bill sale preview")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale preview returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TBillSellPreviewRequest.class)))
    @PostMapping(value = "/sell/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<TBillSellPreviewResponse>> previewTBillSale(@Valid @RequestBody TBillSellPreviewRequest request) {
        return ApiUtil.buildResponse(tBillService.previewTBillSale(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Sell T-bill", description = "Creates a T-bill sale request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "T-bill sold successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TBillSellRequest.class)))
    @PostMapping(value = "/sell", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<TBillSellResponse>> sellTBill(@Valid @RequestBody TBillSellRequest request) {
        return ApiUtil.buildResponse(tBillService.sellTBill(request), HttpStatus.OK.toString(), "Successful");
    }
}
