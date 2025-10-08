package org.meristem.oneapp.usersservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.CreateAdminRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.services.SuperAdminService;
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
@RequestMapping(ApiConstants.CONTEXT_PATH + "super-admin")
@Tag(name = "Super Admin api", description = "This controller manages everything super admin related")
public class SuperAdminController {

    private final SuperAdminService adminService;

    @Operation(summary = "Create admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Allows super admins to create admins")
    })
    @PreAuthorize("hasRole('ROLE_super_admin.admin.create')")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UsersResponse>> createAmin(@RequestBody @Valid CreateAdminRequest request) {
        return ApiUtil.buildResponse(adminService.create(request), HttpStatus.CREATED.toString(), "Admin created successfully");
    }
}
