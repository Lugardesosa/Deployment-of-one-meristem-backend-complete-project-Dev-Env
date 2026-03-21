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
import org.meristem.oneapp.walletservice.domains.responses.WalletAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.WalletBalanceResponse;
import org.meristem.oneapp.walletservice.integrations.UserServiceClient;
import org.meristem.oneapp.walletservice.services.IWalletService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "wallets")
@Tag(name = "Wallet api", description = "This controller manages everything about wallet integration")
public class WalletController {

    private final IWalletService walletService;
    private final UserServiceClient userServiceClient;

    @Operation(summary = "Gets user's balance", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Get balance.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WalletAccountResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")
    })
    @PreAuthorize("hasRole('ROLE_1051')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<WalletBalanceResponse>> getWalletBalance() {
        return ApiUtil.buildResponse(walletService.getAccountBalance(), HttpStatus.OK.toString(), "Successful");
    }
}
