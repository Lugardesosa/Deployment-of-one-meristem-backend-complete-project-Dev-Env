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
import org.meristem.oneapp.walletservice.domains.requests.WithdrawFundRequest;
import org.meristem.oneapp.walletservice.domains.responses.AppResponse;
import org.meristem.oneapp.walletservice.domains.responses.WithdrawFundResponse;
import org.meristem.oneapp.walletservice.services.ITransactionService;
import org.meristem.oneapp.walletservice.services.IWalletService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH)
@Tag(name = "Transaction api", description = "This controller manages everything about Transaction integration")
public class TransactionController {

    private final IWalletService walletService;
    private final ITransactionService transactionService;

    @Operation(summary = "Withdraw money", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Withdraw money.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WithdrawFundResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")
    })
    @PreAuthorize("hasRole('ROLE_1051')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<WithdrawFundResponse>> getWalletBalance(@RequestBody @Valid WithdrawFundRequest request) {
        return ApiUtil.buildResponse(transactionService.withdrawFund(request), HttpStatus.OK.toString(), "Successful");
    }
}
