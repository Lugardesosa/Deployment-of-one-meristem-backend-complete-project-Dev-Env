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
import org.meristem.oneapp.walletservice.domains.requests.WemaAccountQueryRequest;
import org.meristem.oneapp.walletservice.domains.requests.WemaTransactionNotificationRequest;
import org.meristem.oneapp.walletservice.domains.responses.AppResponse;
import org.meristem.oneapp.walletservice.domains.responses.WemaAccountQueryResponse;
import org.meristem.oneapp.walletservice.domains.responses.WemaTransactionResponse;
import org.meristem.oneapp.walletservice.services.TransactionService;
import org.meristem.oneapp.walletservice.services.VirtualAccountService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "wema")
@Tag(name = "Wema api", description = "This controller manages everything about wema virtual account integration")
public class WemaController {

    private final VirtualAccountService virtualAccountService;
    private final TransactionService transactionService;

    @Operation(summary = "Queries Wema account details")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200", description = "Returns the account name for the given account number",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = WemaAccountQueryResponse.class))}
    )})
    @PostMapping(value = "/name-query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WemaAccountQueryResponse accountQuery(@RequestBody @Valid WemaAccountQueryRequest request) {
        return virtualAccountService.queryWemaAccount(request);
    }

    @Operation(summary = "Wema transaction webhook")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200", description = "Acknowledges wema transaction requests",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = WemaTransactionResponse.class))}
    )})
    @PostMapping(value = "/webhook/transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WemaTransactionResponse transactionWebhook(@RequestBody @Valid WemaTransactionNotificationRequest request) {
        return transactionService.handleTransaction(request);
    }
}
