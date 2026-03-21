package org.meristem.oneapp.walletservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.constants.ApiConstants;
import org.meristem.oneapp.walletservice.domains.responses.AppResponse;
import org.meristem.oneapp.walletservice.integrations.requests.WalletCreateRequest;
import org.meristem.oneapp.walletservice.integrations.requests.WalletTransferRequest;
import org.meristem.oneapp.walletservice.integrations.responses.WalletCreateResponse;
import org.meristem.oneapp.walletservice.integrations.responses.WalletTransferResponse;
import org.meristem.oneapp.walletservice.services.IWalletOperationService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.CONTEXT_PATH + "middleware/wallet")
@Tag(name = "Middleware Wallet Operations API", description = "Middleware wallet fund and transfer endpoints.")
public class WalletOperationController {

    private final IWalletOperationService middleWareWalletOperationService;

    @Operation(summary = "Transfer wallet funds")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = WalletTransferRequest.class),
                    examples = @ExampleObject(value = "{\"fromAccountNo\":\"0001234567\",\"toAccountNo\":\"0007654321\",\"amount\":15000.50,\"narration\":\"Wallet transfer for savings\"}")
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WalletTransferResponse.class),
                            examples = @ExampleObject(value = "{\"transferId\":\"TRF-00001234\",\"fromAccountNo\":\"0001234567\",\"toAccountNo\":\"0007654321\",\"amount\":15000.50,\"status\":\"SUCCESS\"}")))
    })
    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<WalletTransferResponse>> transferFunds(@RequestBody @Valid WalletTransferRequest request) {
        return ApiUtil.buildResponse(middleWareWalletOperationService.transferFunds(request), HttpStatus.OK.toString(), "Successful");
    }
}
