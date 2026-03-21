package org.meristem.oneapp.walletservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.constants.ApiConstants;
import org.meristem.oneapp.walletservice.domains.responses.AppResponse;
import org.meristem.oneapp.walletservice.domains.responses.TransactionResponse;
import org.meristem.oneapp.walletservice.services.IWalletTransactionService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
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
@RequestMapping(ApiConstants.CONTEXT_PATH + "middleware/wallet/transactions")
@Tag(name = "Middleware Wallet Transactions API", description = "Middleware wallet transaction endpoints.")
public class WalletTransactionController {

    private final IWalletTransactionService middleWareWalletTransactionService;

    @Operation(summary = "Get recent wallet transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recent transactions returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionResponse.class)))
    })
    @GetMapping(value = "/recent/{accountNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<TransactionResponse>>> getRecentTransactions(@PathVariable String accountNo) {
        return ApiUtil.buildResponse(middleWareWalletTransactionService.getRecentTransactions(accountNo), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get wallet transaction history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionResponse.class)))
    })
    @GetMapping(value = "/{accountNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<TransactionResponse>>> getTransactions(@PathVariable String accountNo) {
        return ApiUtil.buildResponse(middleWareWalletTransactionService.getTransactions(accountNo), HttpStatus.OK.toString(), "Successful");
    }
}
