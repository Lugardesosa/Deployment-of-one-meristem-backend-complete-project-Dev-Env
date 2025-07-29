package org.meristem.oneapp.coreservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservice.constants.ApiConstants;
import org.meristem.oneapp.coreservice.domains.enums.FormName;
import org.meristem.oneapp.coreservice.domains.responses.AppResponse;
import org.meristem.oneapp.coreservice.domains.responses.BankResponse;
import org.meristem.oneapp.coreservice.domains.responses.FormNamesResponse;
import org.meristem.oneapp.coreservice.domains.responses.FormResponse;
import org.meristem.oneapp.coreservice.services.FormService;
import org.meristem.oneapp.coreservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping(ApiConstants.CONTEXT_PATH + "forms")
@RestController
@Tag(name = "Admin api", description = "This controller manages everything forms")
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    @Operation(summary = "Get a form or forms", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get a form and its properties")})
    @PreAuthorize("hasRole('ROLE_users.forms.get')")
    @GetMapping(value = "/{formName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<FormResponse>> getForm(@PathVariable FormName formName) {
        return ApiUtil.buildResponse(formService.getForm(formName), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get a form or forms", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get a form and its properties")})
    @PreAuthorize("hasRole('ROLE_users.forms.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FormNamesResponse>>> getFormNames() {
        return ApiUtil.buildResponse(formService.getFormNames(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get banks", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get bank names")})
    @PreAuthorize("hasRole('ROLE_users.banks.get')")
    @GetMapping(value = "/banks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<BankResponse>>> getBankNames() {
        return ApiUtil.buildResponse(formService.getBankNames(), HttpStatus.OK.toString(), "Successful");
    }
}
