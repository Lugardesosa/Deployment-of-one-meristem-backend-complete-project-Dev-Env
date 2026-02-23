package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.services.IAdminService;
import org.meristem.oneapp.usersservice.services.implementations.AmlService;
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

    private final IAdminService adminService;
    private final AmlService amlService;

    @Operation(summary = "Update next of kin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows admins to update a next of kin")
    })
    @PreAuthorize("hasRole('ROLE_2000')")
    @PostMapping(value = "/users/next-of-kin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<NextOfKinResponse>> createNextOfKin(@RequestBody @Valid CreateNextOfKinRequest request) {
        return ApiUtil.buildResponse(adminService.updateNextOfKin(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "DOB update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their dob")
    })
    @PreAuthorize("hasRole('ROLE_2002')")
    @PutMapping(value = "/users/dob-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<DobResponse>> updateDob(@RequestBody @Valid DobRequest request) {
        return ApiUtil.buildResponse(adminService.updateDob(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Gender update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their gender")
    })
    @PreAuthorize("hasRole('ROLE_2005')")
    @PutMapping(value = "/users/gender-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<GenderResponse>> updateDob(@RequestBody @Valid GenderRequest request) {
        return ApiUtil.buildResponse(adminService.updateGender(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Create admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Allows system admins to create admins")
    })
    @PreAuthorize("hasRole('ROLE_3000')")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UsersResponse>> createAmin(@RequestBody @Valid CreateAdminRequest request) {
        return ApiUtil.buildResponse(adminService.create(request), HttpStatus.CREATED.toString(), "Admin created successfully");
    }

    @Operation(summary = "Enable admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows system admins to create admins")
    })
    @PreAuthorize("hasRole('ROLE_3008')")
    @PutMapping(value = "/enable-admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> enable(@RequestBody @Valid EnableAdminRequest request) {
        return ApiUtil.buildResponse(adminService.enable(request), HttpStatus.CREATED.toString(), "Successful");
    }


    @Operation(summary = "Assign roles to admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assign roles to admin")
    })
    @PreAuthorize("hasRole('ROLE_3003')")
    @PutMapping(value = "/assign-role", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> assignRole(@RequestBody @Valid AssignAdminRoleRequest request) {
        return ApiUtil.buildResponse(adminService.assignRole(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Assign roles to admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assign roles to admin")
    })
    @PreAuthorize("hasRole('ROLE_3004')")
    @PutMapping(value = "/add-role", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> addRole(@RequestBody @Valid AddRoleRequest request) {
        return ApiUtil.buildResponse(adminService.addRole(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Add permissions ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add permissions ")
    })
    @PreAuthorize("hasRole('ROLE_3004')")
    @PostMapping(value = "/add-permission", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> addPermission(@RequestBody @Valid AddPermissionRequest request) {
        return ApiUtil.buildResponse(adminService.addPermission(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Add permissions to role ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add permissions to role ")
    })
    @PreAuthorize("hasRole('ROLE_3004')")
    @PostMapping(value = "/add-permission-role", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> addPermission(@RequestBody @Valid AddPermissionToRoleRequest request) {
        return ApiUtil.buildResponse(adminService.addPermissionToRole(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Get Roles ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get roles ")
    })
    @PreAuthorize("hasRole('ROLE_3005') OR hasAuthority('SCOPE_roles.get')")
    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<RolesResponse>> getRoles(@RequestParam(name = "userId", required = false) Long userId) {
        return ApiUtil.buildResponse(adminService.getRoles(userId), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Get Permissions ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get permissions ")
    })
    @PreAuthorize("hasRole('ROLE_3005')")
    @GetMapping(value = "/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PermissionsResponse>> getPermissions(@Schema(description = "Pass userId to get permissions for a user") @RequestParam(name = "userId", required = false) Long userId,
                                                                           @Schema(description = "Pass roleId to get permissions for a role") @RequestParam(name = "roleId", required = false) Long roleId) {
        return ApiUtil.buildResponse(adminService.getPermissions(userId, roleId), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Get Admins ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Admins ")
    })
    @PreAuthorize("hasRole('ROLE_3006')")
    @GetMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AdminsResponse>> getAdmins(@Schema(description = "Pass userId to get permissions for a user") @RequestParam(name = "userId", required = false) Long adminId) {
        return ApiUtil.buildResponse(adminService.getAdmins(adminId), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Update Admins ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Admins ")
    })
    @PreAuthorize("hasRole('ROLE_3013')")
    @PutMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateAdmin(@RequestBody UpdateAdminRequest request) {
        return ApiUtil.buildResponse(adminService.updateAdmin(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Get user's aml results ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user's aml results ")
    })
    @PreAuthorize("hasRole('ROLE_4004')")
    @GetMapping(value = "/aml-results", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AmlResponse>> getAmlResults(@RequestParam("userId") Long userId) {
        return ApiUtil.buildResponse(amlService.getAmlResults(userId), HttpStatus.CREATED.toString(), "Successful");
    }


    @Operation(summary = "Get user's aml results ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Approve user's aml results ")
    })
    @PreAuthorize("hasRole('ROLE_4004')")
    @PostMapping(value = "/aml-approval", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AmlApprovalResponse>> approveAmlResults(@RequestBody @Valid AmlApprovalRequest request) {
        return ApiUtil.buildResponse(amlService.approveAmlResults(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Get user's aml results ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user's Approve aml results ")
    })
    @PreAuthorize("hasRole('ROLE_4004')")
    @GetMapping(value = "/aml-approval", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<GetAmlResponse>> getAmlApprovalResults(@RequestParam("userId") Long userId) {
        return ApiUtil.buildResponse(amlService.getAmlApprovalResults(userId), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Save an occupation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "")
    })
    @PreAuthorize("hasAuthority('ROLE_3008')")
    @PostMapping(value = "/occupations", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> occupations(@Valid @RequestBody AddOccupationRequest request) {
        return ApiUtil.buildResponse(adminService.addOccupations(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Save a source of income")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Save a source of income")
    })
    @PreAuthorize("hasAuthority('ROLE_3008')")
    @PostMapping(value = "/source_of_income", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> sourceOfIncome(@Valid @RequestBody AddSourceOfIncomeRequest request) {
        return ApiUtil.buildResponse(adminService.addSourceOfIncome(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "get user address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get user address")
    })
    @PreAuthorize("hasAuthority('ROLE_3014')")
    @GetMapping(value = "/get-address", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UserAddressResponse>> getAddress(@RequestParam Long userId) {
        return ApiUtil.buildResponse(adminService.getAddress(userId), HttpStatus.OK.toString(), "Successful");
    }
}
