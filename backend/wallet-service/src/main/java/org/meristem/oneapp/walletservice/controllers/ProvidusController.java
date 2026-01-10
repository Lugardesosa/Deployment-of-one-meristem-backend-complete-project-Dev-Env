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
import org.meristem.oneapp.walletservice.domains.requests.ProvidusAccountFundedEventRequest;
import org.meristem.oneapp.walletservice.domains.responses.ProvidusTransactionResponse;
import org.meristem.oneapp.walletservice.services.ITransactionService;
import org.meristem.oneapp.walletservice.services.implementations.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "providus")
@Tag(name = "Providus api", description = "This controller manages everything about providus virtual account integration")
public class ProvidusController {

    private final ITransactionService transactionService;

    @Operation(summary = "Wema transaction webhook")
    @ApiResponses(value = {@ApiResponse(
            responseCode = "200", description = "Acknowledges wema transaction requests",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProvidusAccountFundedEventRequest.class))}
    )})
    @PostMapping(value = "/webhook/transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProvidusTransactionResponse transactionWebhook(@RequestBody @Valid ProvidusAccountFundedEventRequest request) {
        return transactionService.handleProvidusTransaction(request);
    }
}
