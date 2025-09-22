package org.meristem.oneapp.usersservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.SignedUrlRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.SignedUrlResponse;
import org.meristem.oneapp.usersservice.services.HuaweiService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "document")
@Tag(name = "Document api", description = "This controller manages everything documents")
public class DocumentController {

    private final HuaweiService huaweiService;

    @Operation(summary = "Generate signed url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to generate signed url")
    })
    @PreAuthorize("hasRole('ROLE_users.generate_signed_url')")
    @PostMapping(value = "/generate-signed-url", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SignedUrlResponse>> generateSignedUrl(@RequestBody @Valid SignedUrlRequest request) {
        return ApiUtil.buildResponse(huaweiService.getSignedUrl(request), HttpStatus.OK.toString(), "Successful");
    }
}
