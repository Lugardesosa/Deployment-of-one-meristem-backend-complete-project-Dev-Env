package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.services.AdminService;
import org.meristem.oneapp.usersservice.services.OnboardingService;
import org.meristem.oneapp.usersservice.services.UsersService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "admin")
@Tag(name = "Admin api", description = "This controller manages everything admin related")
public class AdminController {

    private final AdminService adminService;
    private final UsersService usersService;

    @Operation(summary = "Create admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Allows super admins to create admins")
    })
    @PreAuthorize("hasRole('ROLE_super_admin.admin.create')")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UsersResponse>> createAmin(@RequestBody @Valid CreateAdminRequest request) {
        return ApiUtil.buildResponse(adminService.create(request), HttpStatus.CREATED.toString(), "Admin created successfully");
    }

    @Operation(summary = "Update next of kin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows admins to update a next of kin")
    })
    @PreAuthorize("hasRole('ROLE_admin.next_of_kin.update')")
    @PutMapping(value = "/users/next-of-kin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<NextOfKinResponse>> createNextOfKin(@RequestBody @Valid CreateNextOfKinRequest request) {
        return ApiUtil.buildResponse(adminService.updateNextOfKin(request), HttpStatus.OK.toString(), "Successful");
    }

//    @Operation(summary = "Password reset")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Allows admins to reset their password")
//    })
//    @PutMapping(value = "/password-reset", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AppResponse<PasswordResetResponse>> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
//        return ApiUtil.buildResponse(usersService.resetPassword(request), HttpStatus.OK.toString(), "Successful");
//    }
//
//    @Operation(summary = "Password update")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Allows admins to update their password")
//    })
//    @PreAuthorize("hasRole('ROLE_users.password_update')")
//    @PutMapping(value = "/password-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AppResponse<UpdatePasswordResponse>> updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
//        return ApiUtil.buildResponse(usersService.updatePassword(request), HttpStatus.OK.toString(), "Successful");
//    }
}
