package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.CreateAdminRequest;
import org.meristem.oneapp.usersservice.domains.requests.CreateNextOfKinRequest;
import org.meristem.oneapp.usersservice.domains.requests.DobRequest;
import org.meristem.oneapp.usersservice.domains.requests.GenderRequest;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.services.AdminService;
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

    @Operation(summary = "Update next of kin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows admins to update a next of kin")
    })
    @PreAuthorize("hasRole('ROLE_admin.next_of_kin.update')")
    @PutMapping(value = "/users/next-of-kin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<NextOfKinResponse>> createNextOfKin(@RequestBody @Valid CreateNextOfKinRequest request) {
        return ApiUtil.buildResponse(adminService.updateNextOfKin(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "DOB update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their dob")
    })
    @PreAuthorize("hasRole('ROLE_admin.change.dob')")
    @PutMapping(value = "/users/dob-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<DobResponse>> updateDob(@RequestBody @Valid DobRequest request) {
        return ApiUtil.buildResponse(adminService.updateDob(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Gender update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their gender")
    })
    @PreAuthorize("hasRole('ROLE_admin.change.gender')")
    @PutMapping(value = "/users/gender-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<GenderResponse>> updateDob(@RequestBody @Valid GenderRequest request) {
        return ApiUtil.buildResponse(adminService.updateGender(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Create admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Allows system admins to create admins")
    })
    @PreAuthorize("hasRole('ROLE_system.admin.create')")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UsersResponse>> createAmin(@RequestBody @Valid CreateAdminRequest request) {
        return ApiUtil.buildResponse(adminService.create(request), HttpStatus.CREATED.toString(), "Admin created successfully");
    }
}
