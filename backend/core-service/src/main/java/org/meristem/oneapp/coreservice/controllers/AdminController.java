package org.meristem.oneapp.coreservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservice.constants.ApiConstants;
import org.meristem.oneapp.coreservice.domains.requests.UpdateSelectionRequest;
import org.meristem.oneapp.coreservice.domains.responses.AppResponse;
import org.meristem.oneapp.coreservice.domains.requests.UpdateFormRequest;
import org.meristem.oneapp.coreservice.domains.responses.UpdateFormResponse;
import org.meristem.oneapp.coreservice.domains.responses.UpdateSelectionResponse;
import org.meristem.oneapp.coreservice.services.AdminService;
import org.meristem.oneapp.coreservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(ApiConstants.CONTEXT_PATH + "admin")
@RestController
@Tag(name = "Admin api", description = "This controller manages everything admin related")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Add or remove form Selection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows super admins to add or remove form selections")
    })
    @PreAuthorize("hasRole('ROLE_admin.selection.update')")
    @PutMapping(value = "/update-selections", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateSelectionResponse>> updateSelections(@Valid @RequestBody UpdateSelectionRequest request) {

        return ApiUtil.buildResponse(adminService.updateSelection(request), HttpStatus.OK.toString(), ApiConstants.SUCCESSFUL_MESSAGE);
    }

//    @Operation(summary = "Add or remove form items")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Allows super admins to add or remove form items")
//    })
//    @PreAuthorize("hasRole('ROLE_admin.form.items.update')")
//    @PutMapping(value = "/update-forms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AppResponse<UpdateFormResponse>> updateForms(@Valid @RequestBody UpdateFormRequest request) {
//
//        return ApiUtil.buildResponse(adminService.updateForm(request), HttpStatus.OK.toString(), ApiConstants.SUCCESSFUL_MESSAGE);
//    }
}
