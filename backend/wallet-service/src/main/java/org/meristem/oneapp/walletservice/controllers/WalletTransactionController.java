package org.meristem.oneapp.walletservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.constants.ApiConstants;
import org.meristem.oneapp.walletservice.domains.requests.TransactionRequest;
import org.meristem.oneapp.walletservice.domains.responses.AppResponse;
import org.meristem.oneapp.walletservice.domains.responses.TransactionResponse;
import org.meristem.oneapp.walletservice.services.IWalletTransactionService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "transactions")
@Tag(name = "Middleware Wallet Transactions API", description = "Middleware wallet transaction endpoints.")
public class WalletTransactionController {

    private final IWalletTransactionService middleWareWalletTransactionService;

    @Operation(summary = "Get recent wallet transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recent transactions returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionResponse.class)))
    })
    @PreAuthorize("hasAuthority('ROLE_1050')")
    @GetMapping(value = "/recent/{walletId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<TransactionResponse>>> getRecentTransactions(@PathVariable String walletId) {
        return ApiUtil.buildResponse(middleWareWalletTransactionService.getRecentTransactions(walletId), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get wallet transaction history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionResponse.class)))
    })
    @PreAuthorize("hasAuthority('ROLE_1050')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<TransactionResponse>>> getTransactions(@RequestBody TransactionRequest request) {
        return ApiUtil.buildResponse(middleWareWalletTransactionService.getTransactions(request), HttpStatus.OK.toString(), "Successful");
    }
}
