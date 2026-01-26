package org.meristem.oneapp.trusteesservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.trusteesservice.constants.ApiConstants;
import org.meristem.oneapp.trusteesservice.domains.enums.FormName;
import org.meristem.oneapp.trusteesservice.domains.enums.GeneralFormType;
import org.meristem.oneapp.trusteesservice.domains.responses.AppResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.BankResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.FormNamesResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.FormResponse;
import org.meristem.oneapp.trusteesservice.services.IFormService;
import org.meristem.oneapp.trusteesservice.services.implementations.FormService;
import org.meristem.oneapp.trusteesservice.utils.ApiUtil;
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

    private final IFormService formService;

    @Operation(summary = "Get a form or forms", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get a form and its properties")})
    @PreAuthorize("hasRole('ROLE_1028')")
    @GetMapping(value = "/{formName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<FormResponse>> getForm(@PathVariable FormName formName) {
        return ApiUtil.buildResponse(formService.getForm(formName), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get a form ", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get a form and its properties")})
    @PreAuthorize("hasRole('ROLE_1028')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<FormNamesResponse>>> getFormNames(@RequestParam(name = "formName") GeneralFormType generalFormType) {
        return ApiUtil.buildResponse(formService.getFormNames(generalFormType), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get banks", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get bank names")})
    @PreAuthorize("hasRole('ROLE_1029')")
    @GetMapping(value = "/banks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<BankResponse>>> getBankNames() {
        return ApiUtil.buildResponse(formService.getBankNames(), HttpStatus.OK.toString(), "Successful");
    }
}
