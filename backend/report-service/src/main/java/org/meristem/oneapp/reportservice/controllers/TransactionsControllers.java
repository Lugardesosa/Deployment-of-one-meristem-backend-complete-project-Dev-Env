package org.meristem.oneapp.reportservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.reportservice.constants.ApiConstants;
import org.meristem.oneapp.reportservice.domains.requests.TransactionsRequest;
import org.meristem.oneapp.reportservice.domains.responses.AppResponse;
import org.meristem.oneapp.reportservice.domains.responses.TransactionsResponse;
import org.meristem.oneapp.reportservice.models.Transactions;
import org.meristem.oneapp.reportservice.services.TransactionsService;
import org.meristem.oneapp.reportservice.utils.ApiUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "transactions")
@Tag(name = "Transactions Controller", description = "Manages transactions query from the report microservice")
public class TransactionsControllers {

    private final TransactionsService transactionsService;

    @Operation(summary = "Gets user's transactions", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gets transactions",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionsResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")
    })
    @PreAuthorize("hasRole('ROLE_users.transactions.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Page<Transactions>>>  getTransactions(TransactionsRequest request) {
        return ApiUtil.buildResponse(transactionsService.getTransactions(request), HttpStatus.OK.toString(), "Successful");
    }
}
