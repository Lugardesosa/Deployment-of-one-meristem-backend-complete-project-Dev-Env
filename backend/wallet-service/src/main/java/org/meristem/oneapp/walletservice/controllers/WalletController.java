package org.meristem.oneapp.walletservice.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.domains.responses.AppResponse;
import org.meristem.oneapp.walletservice.domains.responses.WalletBalanceResponse;
import org.meristem.oneapp.walletservice.services.IWalletService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Wallet api", description = "This controller manages everything about wallet integration")
public class WalletController {

    private final IWalletService walletService;


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<WalletBalanceResponse>> getWalletBalance() {

        return ApiUtil.buildResponse(walletService.getAccountBalance(), HttpStatus.OK.toString(), "Successful");
    }
}
