package org.meristem.oneapp.walletservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.constants.ApiConstants;
import org.meristem.oneapp.walletservice.domains.requests.AddAccountNumberRequest;
import org.meristem.oneapp.walletservice.domains.responses.AppResponse;
import org.meristem.oneapp.walletservice.domains.responses.WalletAccountResponse;
import org.meristem.oneapp.walletservice.integrations.requests.WithdrawalRequest;
import org.meristem.oneapp.walletservice.integrations.responses.UpdateResponse;
import org.meristem.oneapp.walletservice.services.IWalletAccountService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "accounts")
@Tag(name = "Wallet Accounts API", description = "wallet health and account endpoints.")
public class WalletAccountController {

    private final IWalletAccountService middleWareWalletAccountService;


    @Operation(summary = "Get customer wallet accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer wallet accounts returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WalletAccountResponse.class)))
    })
    @PreAuthorize("hasAuthority('ROLE_1050')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<WalletAccountResponse>>> getAccounts() {
        return ApiUtil.buildResponse(middleWareWalletAccountService.getAccounts(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Add customer's bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer wallet accounts returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WalletAccountResponse.class)))
    })
    @PreAuthorize("hasAuthority('ROLE_1050') AND @authz.ownsWalletId()")
    @GetMapping(value = "/add-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> addAccount(@RequestBody AddAccountNumberRequest request) {
        return ApiUtil.buildResponse(middleWareWalletAccountService.addAccountNo(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Withdraw to bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdraw to bank account",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WalletAccountResponse.class)))
    })
    @PreAuthorize("hasAuthority('ROLE_1050') AND @authz.ownsWalletId()")
    @GetMapping(value = "/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> withdraw(@RequestBody WithdrawalRequest request) {
        return ApiUtil.buildResponse(middleWareWalletAccountService.withdraw(request), HttpStatus.OK.toString(), "Successful");
    }
}
