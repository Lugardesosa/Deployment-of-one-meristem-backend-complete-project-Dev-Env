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
import org.meristem.oneapp.walletservice.domains.responses.VirtualAccountResponse;
import org.meristem.oneapp.walletservice.services.VirtualAccountService;
import org.meristem.oneapp.walletservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "accounts")
@Tag(name = "Account Controller", description = "This controllers manages users account")
public class AccountController {

    private final VirtualAccountService virtualAccountService;

    @Operation(summary = "Gets user's accounts", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Get accounts.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = VirtualAccountResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")
    })
    @PreAuthorize("hasRole('ROLE_users.virtual_accounts.get')")
    @GetMapping(value = "")
    public ResponseEntity<AppResponse<List<VirtualAccountResponse>>> getVirtualAccounts() {
        return ApiUtil.buildResponse(virtualAccountService.getAccounts(), HttpStatus.OK.toString(), "Successful");
    }
}
