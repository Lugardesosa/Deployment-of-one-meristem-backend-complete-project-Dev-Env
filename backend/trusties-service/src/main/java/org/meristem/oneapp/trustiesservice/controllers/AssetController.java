package org.meristem.oneapp.trustiesservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.trustiesservice.constants.ApiConstants;
import org.meristem.oneapp.trustiesservice.domains.enums.Assets;
import org.meristem.oneapp.trustiesservice.domains.requests.*;
import org.meristem.oneapp.trustiesservice.domains.responses.*;
import org.meristem.oneapp.trustiesservice.services.AssetService;
import org.meristem.oneapp.trustiesservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequestMapping(ApiConstants.CONTEXT_PATH + "assets")
@RestController
@RequiredArgsConstructor
@Tag(name = "Asset API", description = "Controls everything asset")
public class AssetController {

    private final AssetService assetService;

    @Operation(summary = "Create a cash asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a cash asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/cash", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<CashResponse>> createCash(@RequestBody @Valid CashRequest cashRequest) {
        return ApiUtil.buildResponse(assetService.saveCash(cashRequest), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a public equities asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a public equities asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/public-equities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PublicEquitiesResponse>> createPublicEquities(@RequestBody @Valid PublicEquitiesRequest request) {
        return ApiUtil.buildResponse(assetService.savePublicEquities(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a private equities asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a private equities asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/private-equities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<EquitiesResponse>> createPrivateEquities(@RequestBody @Valid EquitiesRequest request) {
        return ApiUtil.buildResponse(assetService.savePrivateEquities(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a real estate asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a real estate asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/real-estate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<RealEstateResponse>> createRealEstate(@RequestBody @Valid RealEstateRequest request) {
        return ApiUtil.buildResponse(assetService.saveRealEstate(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a fixed income / money market asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a fixed income / money market asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/money-market", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<MoneyMarketResponse>> createMoneyMarket(@RequestBody @Valid MoneyMarketRequest request) {
        return ApiUtil.buildResponse(assetService.saveMoneyMarket(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a intellectual property asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a intellectual property asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/intellectual-property", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<IntellectualPropertyResponse>> createIntellectualProperty(@RequestBody @Valid IntellectualPropertyRequest request) {
        return ApiUtil.buildResponse(assetService.saveIntellectualProperty(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a alternate asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a alternate asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/alternate-assets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AlternateAssetsResponse>> createAlternateAssets(@RequestBody @Valid AlternateAssetsRequest request) {
        return ApiUtil.buildResponse(assetService.saveAlternateAssets(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a personal asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a personal asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/personal-assets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PersonalAssetsResponse>> createPersonalAssets(@RequestBody @Valid PersonalAssetsRequest request) {
        return ApiUtil.buildResponse(assetService.savePersonalAssets(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a pension asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a pension asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/pension", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PensionResponse>> createPension(@RequestBody @Valid PensionRequest request) {
        return ApiUtil.buildResponse(assetService.savePension(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Create a life insurance asset", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a life insurance asset")})
    @PreAuthorize("hasRole('ROLE_users.asset.create')")
    @PostMapping(value = "/life-insurance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<LifeInsuranceResponse>> createLifeInsurance(@RequestBody @Valid LifeInsuranceRequest request) {
        return ApiUtil.buildResponse(assetService.saveLifeInsurance(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Get an asset (s)", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get an asset (s)",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GetAssetResponse.class)
            )})
    @PreAuthorize("hasRole('ROLE_users.asset.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<GetAssetResponse>> getAssets(@RequestParam(name = "asset-name") Assets assetName, @RequestParam(name = "asset-id", required = false) Long assetId) {
        return ApiUtil.buildResponse(assetService.getAssets(assetName, assetId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get total assets value", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get total assets value",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GetAssetResponse.class)
            )})
    @PreAuthorize("hasRole('ROLE_users.asset.get')")
    @GetMapping(value = "/estimated-value", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<GetAssetValueResponse>> getAssetsValue(@RequestParam(name = "asset-name", required = false) Assets assets) {
        return ApiUtil.buildResponse(assetService.getAssetsValue(assets), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get total assets value", method = "GET")
    @ApiResponse(responseCode = "200", description = "Get total assets value",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GetAssetResponse.class)
            )})
    @PreAuthorize("hasRole('ROLE_users.asset.remove')")
    @DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AssetDeleteResponse>> deleteAsset(@RequestParam(name = "asset-name") Assets assets, @RequestParam(name = "asset-id") Long assetId) {
        return ApiUtil.buildResponse(assetService.deleteAsset(assets, assetId), HttpStatus.OK.toString(), "Successful");
    }
}
