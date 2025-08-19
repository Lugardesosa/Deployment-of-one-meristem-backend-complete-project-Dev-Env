package org.meristem.oneapp.trustiesservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.trustiesservice.constants.ApiConstants;
import org.meristem.oneapp.trustiesservice.domains.enums.FormName;
import org.meristem.oneapp.trustiesservice.domains.enums.GeneralFormType;
import org.meristem.oneapp.trustiesservice.domains.responses.AppResponse;
import org.meristem.oneapp.trustiesservice.domains.responses.BankResponse;
import org.meristem.oneapp.trustiesservice.domains.responses.FormNamesResponse;
import org.meristem.oneapp.trustiesservice.domains.responses.FormResponse;
import org.meristem.oneapp.trustiesservice.services.FormService;
import org.meristem.oneapp.trustiesservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping(ApiConstants.CONTEXT_PATH + "forms")
@RestController
@Tag(name = "Form api", description = "This controller manages everything forms")
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

    @Operation(summary = "Get a form ", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get a form and its properties")})
    @PreAuthorize("hasRole('ROLE_users.forms.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FormNamesResponse>>> getFormNames(@RequestParam(name = "formName") GeneralFormType generalFormType) {
        return ApiUtil.buildResponse(formService.getFormNames(generalFormType), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get banks", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get bank names")})
    @PreAuthorize("hasRole('ROLE_users.banks.get')")
    @GetMapping(value = "/banks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<BankResponse>>> getBankNames() {
        return ApiUtil.buildResponse(formService.getBankNames(), HttpStatus.OK.toString(), "Successful");
    }
}
