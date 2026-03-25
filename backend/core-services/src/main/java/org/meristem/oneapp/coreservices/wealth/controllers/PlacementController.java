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
import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementCreateRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementPreviewRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementTopupRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;
import org.meristem.oneapp.coreservices.wealth.services.IPlacementService;
import org.meristem.oneapp.coreservices.wealth.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "placements")
@Tag(name = "Wealth Placements", description = "Endpoints for placement investment operations")
public class PlacementController {

    private final IPlacementService placementService;

    @Operation(summary = "List placement products", description = "Returns all placement products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Placement products returned successfully")
    })
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<PlacementProductResponse>>> getPlacementProducts() {
        return ApiUtil.buildResponse(placementService.getPlacementProducts(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get placement rate", description = "Returns rate details for a placement product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Placement rate returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid productId")
    })
    @GetMapping(value = "/rates/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PlacementRateResponse>> getPlacementRate(@PathVariable String productId) {
        return ApiUtil.buildResponse(placementService.getPlacementRate(productId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Preview placement", description = "Returns placement preview computation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Placement preview returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PlacementPreviewRequest.class)))
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PlacementPreviewResponse>> previewPlacement(@Valid @RequestBody PlacementPreviewRequest request) {
        return ApiUtil.buildResponse(placementService.previewPlacement(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Create placement", description = "Creates a placement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Placement created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PlacementCreateRequest.class)))
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PlacementActionResponse>> createPlacement(@Valid @RequestBody PlacementCreateRequest request) {
        return ApiUtil.buildResponse(placementService.createPlacement(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Top up placement", description = "Adds funds to an existing placement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Placement topped up successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PlacementTopupRequest.class)))
    @PostMapping(value = "/topup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PlacementActionResponse>> topUpPlacement(@Valid @RequestBody PlacementTopupRequest request) {
        return ApiUtil.buildResponse(placementService.topUpPlacement(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "List customer placements", description = "Returns all placements by customerId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer placements returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customerId")
    })
    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<PlacementSummaryResponse>>> getPlacementsByCustomer(@PathVariable String customerId) {
        return ApiUtil.buildResponse(placementService.getPlacementsByCustomer(customerId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "List placement transactions", description = "Returns transactions for a placement account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Placement transactions returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid fundAccountId")
    })
    @GetMapping(value = "/transactions/{fundAccountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<PlacementTransactionResponse>>> getPlacementTransactions(@PathVariable String fundAccountId) {
        return ApiUtil.buildResponse(placementService.getPlacementTransactions(fundAccountId), HttpStatus.OK.toString(), "Successful");
    }
}
