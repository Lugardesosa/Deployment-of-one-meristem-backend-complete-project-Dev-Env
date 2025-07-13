package org.meristem.oneapp.usersservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.services.NextOfKinService;
import org.meristem.oneapp.usersservice.services.UsersService;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "base")
@Tag(name = "Users api", description = "This controller manages everything users")
public class UsersController {

    private final UsersService usersService;
    private final NextOfKinService nextOfKinService;


    @Operation(summary = "Creates a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserRequest.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")

    })
    @PreAuthorize("hasAuthority('SCOPE_create_user')")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UsersResponse>> createUser(@RequestBody @Valid CreateUserRequest userRequest) {
         return ApiUtil.buildResponse(usersService.createUser(userRequest), HttpStatus.CREATED.toString(), "Created successfully.");
    }

    @Operation(summary = "Gets users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a user.")
    })
    @PreAuthorize("hasAuthority('SCOPE_users.get') OR hasRole('ROLE_users.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UsersResponse>> getUser() {
        return ApiUtil.buildResponse(usersService.getUser(), HttpStatus.OK.toString(), "Successful.");
    }

    @Operation(summary = "Update user's phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their phone number")
    })
    @PreAuthorize("hasRole('ROLE_users.phone-number.update')")
    @PutMapping(value = "/phone-number", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdatePhoneNumberResponse>> updatePhoneNumber(@RequestBody @Valid UpdatePhoneNumberRequest request) {
        return ApiUtil.buildResponse(usersService.updatePhoneNumber(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Upload profile picture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to upload profile pictures")
    })
    @PreAuthorize("hasRole('ROLE_users.p_picture.post')")
    @PutMapping(value = "/image", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateImageResponse>> updateImage(@RequestBody @Valid UpdateImageRequest request) {
        return ApiUtil.buildResponse(usersService.updateImage(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to reset their password")
    })
    @PreAuthorize("hasAuthority('SCOPE_password_reset')")
    @PutMapping(value = {"/password-reset", "/admin/password-reset"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PasswordResetResponse>> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        return ApiUtil.buildResponse(usersService.resetPassword(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Create next of kin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Allows users to create a next of kin")
    })
    @PreAuthorize("hasRole('ROLE_users.next_of_kin.create')")
    @PostMapping(value = "/next-of-kin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<NextOfKinResponse>> createNextOfKin(@RequestBody @Valid CreateNextOfKinRequest request) {
        return ApiUtil.buildResponse(nextOfKinService.createNextOfKin(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Password update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their password")
    })
    @PreAuthorize("hasAnyRole('ROLE_users.change-password', 'ROLE_admin.change.password')")
    @PutMapping(value = {"/password-update", "/admin/password-update"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        return ApiUtil.buildResponse(usersService.updatePassword(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Pin update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their pin")
    })
    @PreAuthorize("hasRole('ROLE_users.change.pin')")
    @PutMapping(value = "/pin-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PinResponse>> updatePin(@RequestBody @Valid PinRequest request) {
        return ApiUtil.buildResponse(usersService.updatePin(request), HttpStatus.OK.toString(), "Successful");
    }


    @Operation(summary = "Get Avatars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to get all avatars")
    })
    @PreAuthorize("hasRole('ROLE_users.get.images')")
    @GetMapping(value = "/avatars", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<SignedUrlResponse>>> getAvatars() {
        return ApiUtil.buildResponse(usersService.getAvatarUrls(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Deactivate users account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to deactivate their accounts")
    })
    @PreAuthorize("hasRole('ROLE_users.deactivate.account')")
    @PutMapping(value = "/deactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AccountDeactivationResponse>> deactivateUser() {
        return ApiUtil.buildResponse(usersService.deactivateUser(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get next of kin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to get their next of kin")
    })
    @PreAuthorize("hasRole('ROLE_users.next_of_kin.get')")
    @GetMapping(value = "/next-of-kin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<NextOfKinResponse>> getNextOfKin() {
        return ApiUtil.buildResponse(nextOfKinService.getNextOfKin(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Update state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their state of origin")
    })
    @PreAuthorize("hasRole('ROLE_users.state.update')")
    @PutMapping(value = "/state", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateStateOfOrigin(@Valid @RequestBody StateUpdateRequest request) {
        return ApiUtil.buildResponse(usersService.updateStateOfOrigin(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Update Country")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their state of origin")
    })
    @PreAuthorize("hasRole('ROLE_users.country.update')")
    @PutMapping(value = "/country", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateCountryOfOrigin(@Valid @RequestBody CountryUpdateRequest request) {
        return ApiUtil.buildResponse(usersService.updateCountryOfOrigin(request), HttpStatus.OK.toString(), "Successful");
    }
}
