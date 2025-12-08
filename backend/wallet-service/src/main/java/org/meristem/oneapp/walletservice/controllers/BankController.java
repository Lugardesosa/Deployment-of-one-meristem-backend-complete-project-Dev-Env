package org.meristem.oneapp.walletservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.constants.ApiConstants;
import org.meristem.oneapp.walletservice.domains.enums.ProviderCode;
import org.meristem.oneapp.walletservice.domains.requests.BankAccountRequest;
import org.meristem.oneapp.walletservice.domains.responses.AppResponse;
import org.meristem.oneapp.walletservice.domains.responses.BankAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.BankCodeResponse;
import org.meristem.oneapp.walletservice.services.BankService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "banks")
@RequiredArgsConstructor
@Tag(name = "Banks api", description = "This controller manages everything about banks")
public class BankController {

    private final BankService bankService;

    @Operation(summary = "Get bank account details")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200", description = "Resolves bank account details",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BankAccountResponse.class))}
    )})
    @PreAuthorize("hasAuthority('ROLE_users.resolve.bank')")
    @PostMapping(value = "/resolve-account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<BankAccountResponse>> resolveAccount(@RequestBody @Valid BankAccountRequest request) {
        return ApiUtil.buildResponse(bankService.resolveAccount(request), HttpStatus.OK.toString(), "Successful");
    }


    @Operation(summary = "Get bank account details")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200", description = "Resolves bank account details",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BankCodeResponse.class))}
    )})
    @PreAuthorize("hasAuthority('ROLE_users.banks.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<BankCodeResponse>>> getBanks(@RequestParam ProviderCode providerCode) {
        return ApiUtil.buildResponse(bankService.getBanks(providerCode), HttpStatus.OK.toString(), "Successful");
    }
}
