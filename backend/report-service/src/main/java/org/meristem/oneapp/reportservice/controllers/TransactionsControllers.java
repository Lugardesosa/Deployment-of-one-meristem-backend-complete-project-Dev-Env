package org.meristem.oneapp.reportservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.reportservice.constants.ApiConstants;
import org.meristem.oneapp.reportservice.domains.requests.TransactionResponse;
import org.meristem.oneapp.reportservice.domains.requests.TransactionsRequest;
import org.meristem.oneapp.reportservice.domains.responses.AppResponse;
import org.meristem.oneapp.reportservice.domains.responses.PageTransactionsResponse;
import org.meristem.oneapp.reportservice.domains.responses.TransactionsResponse;
import org.meristem.oneapp.reportservice.services.ITransactionsService;
import org.meristem.oneapp.reportservice.utils.ApiUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "transactions")
@Tag(name = "Transactions Controller", description = "Manages transactions query from the report microservice")
public class TransactionsControllers {

    private final ITransactionsService transactionsService;

    @Operation(summary = "Gets user's transactions", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gets transactions",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionsResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")
    })
    @PreAuthorize("hasRole('ROLE_1039')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Page<TransactionResponse>>>  getTransactions(TransactionsRequest request) {
        return ApiUtil.buildResponse(transactionsService.getTransaction(request), HttpStatus.OK.toString(), "Successful");
    }
}
