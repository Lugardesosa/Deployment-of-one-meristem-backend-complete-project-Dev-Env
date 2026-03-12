package org.meristem.oneapp.usersservice.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.services.INextOfKinService;
import org.meristem.oneapp.usersservice.services.IUsersService;
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

    private final IUsersService usersService;
    private final INextOfKinService nextOfKinService;

    @Operation(summary = "Creates a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the user.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")

    })
    @PreAuthorize("hasAuthority('SCOPE_create_user')")
    @PostMapping(value = "/individual", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> createUserIndividual(@RequestBody @Valid CreateUserRequest userRequest) {
         return ApiUtil.buildResponse(usersService.create(userRequest), HttpStatus.CREATED.toString(), "Created successfully.");
    }

    @Operation(summary = "Creates a joint user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created a joint user account.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")

    })
    @PreAuthorize("hasAuthority('SCOPE_create_user')")
    @PostMapping(value = "/joint", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> createUserJoint(@RequestBody @Valid CreateJointUserRequest userRequest) {
        return ApiUtil.buildResponse(usersService.createJoint(userRequest), HttpStatus.CREATED.toString(), "Created successfully.");
    }


    @Operation(summary = "Creates a minor account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created a minor account.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")

    })
    @PreAuthorize("hasRole('ROLE_1000')")
    @PostMapping(value = "/minor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> createUserDependent(@RequestBody @Valid CreateUserDependentRequest userRequest) {
        return ApiUtil.buildResponse(usersService.createUserDependent(userRequest), HttpStatus.CREATED.toString(), "Created successfully.");
    }

    @Operation(summary = "Verifies users email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Verifies users email.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserRequest.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")

    })
    @PreAuthorize("hasAuthority('SCOPE_create_user')")
    @PutMapping(value = "/verify-email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> verifyEmail(@RequestBody @Valid VerifyOtpRequest request) {
        return ApiUtil.buildResponse(usersService.verifyEmail(request), HttpStatus.CREATED.toString(), "Updated successfully.");
    }

    @Operation(summary = "Set a user's password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set a user's password after account creation.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SetPasswordRequest.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")

    })
    @PreAuthorize("hasAuthority('SCOPE_create_user')")
    @PutMapping(value = "/set-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> setPassword(@RequestBody @Valid SetPasswordRequest request) {
        return ApiUtil.buildResponse(usersService.setPasswordInternal(request), HttpStatus.OK.toString(), "Successful.");
    }

    @Operation(summary = "Get a user's joint account details response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a user's joint account details response.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JointAccountDetailsResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")
    })
    @PreAuthorize("hasRole('ROLE_1000')")
    @GetMapping(value = "/joint-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<JointAccountDetailsResponse>>> getJointAccountDetails() {
        return ApiUtil.buildResponse(usersService.getJointAccountDetails(), HttpStatus.OK.toString(), "Successful.");
    }

    @Operation(summary = "Get a user's joint account details response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a user's joint account details response.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JointAccountDetailsResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")
    })
    @PreAuthorize("hasAuthority('SCOPE_create_user')")
    @PostMapping(value = "/query-existing", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> queryExistingUser(@RequestBody @Valid QueryExistingUserRequest request) {
        return ApiUtil.buildResponse(usersService.queryExistingUser(request), HttpStatus.OK.toString(), "Successful.");
    }

//    @Operation(summary = "Get users investment instrument")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Get users investment instrument")
//    })
//    @PreAuthorize("hasRole('ROLE_1000')")
//    @GetMapping(value = "/instrument", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AppResponse<UsersResponse>> getInvestmentInstrument() {
//        return ApiUtil.buildResponse(usersService.getInvestmentInstrument(), HttpStatus.OK.toString(), "Request successful");
//    }


    @Operation(summary = "Set a user's cscs and chn number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set a user's cscs number.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateCscsRequest.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - Cscs and Chn number could not be updated")

    })
    @PreAuthorize("hasRole('ROLE_1048')")
    @PutMapping(value = "/cscs-chn-update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateCscs(@RequestBody @Valid UpdateCscsRequest request) {
        return ApiUtil.buildResponse(usersService.updateCscs(request), HttpStatus.OK.toString(), "Successful.");
    }


    @Operation(summary = "Sign user up for this product/investement instrument.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sign user up for this product/investement instrument.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateCscsRequest.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - Sign user up for this product/investement instrument failed")

    })
    @PreAuthorize("hasRole('ROLE_1048')")
    @PostMapping(value = "/onboard-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> onboardOnProduct() {
        return ApiUtil.buildResponse(usersService.onboardOnProduct(), HttpStatus.OK.toString(), "Successful.");
    }

    @Operation(summary = "Set a user's email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set a user's email only when it has not been verified.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateEmailRequest.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")

    })
    @PreAuthorize("hasAuthority('SCOPE_users.email.update')")
    @PutMapping(value = "/update-email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateEmail(@RequestBody @Valid UpdateEmailRequest request) {
        return ApiUtil.buildResponse(usersService.updateEmail(request), HttpStatus.OK.toString(), "Created successfully.");
    }

    @Operation(summary = "Set a user's email of the primary account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set a user's email only when it has not been verified of the primary account.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateEmailRequest.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")

    })
    @PreAuthorize("hasAuthority('SCOPE_users.email.update')")
    @PutMapping(value = "/update-email-joint", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateEmailJoint(@RequestBody @Valid UpdateEmailRequest request) {
        return ApiUtil.buildResponse(usersService.updateJointPrimaryEmail(request), HttpStatus.OK.toString(), "Created successfully.");
    }

    @Operation(summary = "Gets users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a user.")
    })
    @PreAuthorize("hasAuthority('SCOPE_users.get') OR hasRole('ROLE_1000')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UsersResponse>> getUser() {
        return ApiUtil.buildResponse(usersService.getUser(), HttpStatus.OK.toString(), "Successful.");
    }

    @Operation(summary = "Update user's phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their phone number")
    })
    @PreAuthorize("hasRole('ROLE_1040')")
    @PutMapping(value = "/phone-number", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdatePhoneNumberResponse>> updatePhoneNumber(@RequestBody @Valid UpdatePhoneNumberRequest request) {
        return ApiUtil.buildResponse(usersService.updatePhoneNumber(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Upload profile picture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to upload profile pictures")
    })
    @PreAuthorize("hasRole('ROLE_1035')")
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
    @PreAuthorize("hasRole('ROLE_1033')")
    @PostMapping(value = "/next-of-kin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<NextOfKinResponse>> createNextOfKin(@RequestBody @Valid CreateNextOfKinRequest request) {
        return ApiUtil.buildResponse(nextOfKinService.createNextOfKin(request), HttpStatus.CREATED.toString(), "Successful");
    }

    @Operation(summary = "Password update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their password")
    })
    @PreAuthorize("hasAnyRole('ROLE_1026', 'ROLE_2001')")
    @PutMapping(value = {"/password-update", "/admin/password-update"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        return ApiUtil.buildResponse(usersService.updatePassword(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Pin update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their pin")
    })
    @PreAuthorize("hasRole('ROLE_1042')")
    @PutMapping(value = "/pin-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<PinResponse>> updatePin(@RequestBody @Valid PinRequest request) {
        return ApiUtil.buildResponse(usersService.updatePin(request), HttpStatus.OK.toString(), "Successful");
    }


    @Operation(summary = "Get Avatars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to get all avatars")
    })
    @PreAuthorize("hasRole('ROLE_1046')")
    @GetMapping(value = "/avatars", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<SignedUrlResponse>>> getAvatars() {
        return ApiUtil.buildResponse(usersService.getAvatarUrls(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Deactivate users account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to deactivate their accounts")
    })
    @PreAuthorize("hasRole('ROLE_1045')")
    @PutMapping(value = "/deactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AccountDeactivationResponse>> deactivateUser() {
        return ApiUtil.buildResponse(usersService.deactivateUser(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get next of kin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to get their next of kin")
    })
    @PreAuthorize("hasRole('ROLE_1034')")
    @GetMapping(value = "/next-of-kin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<NextOfKinResponse>> getNextOfKin() {
        return ApiUtil.buildResponse(nextOfKinService.getNextOfKin(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Update state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their state of origin")
    })
    @PreAuthorize("hasRole('ROLE_1014')")
    @PutMapping(value = "/state", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateStateOfOrigin(@Valid @RequestBody StateUpdateRequest request) {
        return ApiUtil.buildResponse(usersService.updateStateOfOrigin(request), HttpStatus.OK.toString(), "Successful");
    }

//    @Hidden
//    @Operation(summary = "Update biometric log in")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Allows users to update their biometric log in")
//    })
//    @PreAuthorize("hasRole('ROLE_1015')")
//    @PutMapping(value = "/biometric-login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AppResponse<UpdateResponse>> updateBiometricOfOrigin(@Valid @RequestBody BiometricLoginUpdateRequest request) {
//        return ApiUtil.buildResponse(usersService.updateBiometricOfOrigin(request), HttpStatus.OK.toString(), "Successful");
//    }

    @Hidden
    @Operation(summary = "Update Country")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to update their state of origin")
    })
    @PreAuthorize("hasRole('ROLE_1016')")
    @PutMapping(value = "/country", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateCountryOfOrigin(@Valid @RequestBody CountryUpdateRequest request) {
        return ApiUtil.buildResponse(usersService.updateCountryOfOrigin(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Mark an instrument/subsidiary as visited")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mark an instrument/subsidiary as visited")
    })
    @PreAuthorize("hasRole('ROLE_1022')")
    @PutMapping(value = "/instrument-accessed", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateUserInstrument(@Valid @RequestBody UserInstrumentRequest request) {
        return ApiUtil.buildResponse(usersService.updateUserInstrument(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Mark an subsidiary option as visited")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mark an subsidiary option as visited")
    })
    @PreAuthorize("hasRole('ROLE_1022')")
    @PutMapping(value = "/option-accessed", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateOptionAccessed(@Valid @RequestBody OptionAccessedRequest request) {
        return ApiUtil.buildResponse(usersService.updateOptionAccessed(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Approve or revoke data sharing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Approve or revoke data sharing")
    })
    @PreAuthorize("hasRole('ROLE_1013')")
    @PutMapping(value = "/share-data", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateDataSharing(@Valid @RequestBody DataSharingRequest request) {
        return ApiUtil.buildResponse(usersService.updateDataSharing(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Approve all data sharing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Approve all data sharing")
    })
    @PreAuthorize("hasAuthority('SCOPE_share_all_data')")
    @PutMapping(value = "/share-all-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> updateDataSharing(@RequestBody @Valid ShareAllDataRequest request) {
        return ApiUtil.buildResponse(usersService.updateDataSharing(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Get existing subsidiary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get existing subsidiary")
    })
    @PreAuthorize("hasAuthority('SCOPE_get.existing_intrument')")
    @GetMapping(value = "/existing-subsidiary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<ExistingInstrumentResponse>>> existingInstruments() {
        return ApiUtil.buildResponse(usersService.existingInstruments(), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Approve all data sharing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Approve all data sharing")
    })
    @PreAuthorize("hasRole('ROLE_1011')")
    @PutMapping(value = "/interest-free", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> interestFree(@Valid @RequestBody InterestSharingRequest request) {
        return ApiUtil.buildResponse(usersService.interestFree(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Verifies users pin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verifies users pin")
    })
    @PreAuthorize("hasAuthority('SCOPE_verify.pin') OR hasRole('ROLE_1043')")
    @PutMapping(value = "/verify-pin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> verifyPin(@Valid @RequestBody VerifyPinRequest request) {
        return ApiUtil.buildResponse(usersService.verifyPin(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Verifies users password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verifies users password")
    })
    @PreAuthorize("hasRole('ROLE_1044')")
    @PutMapping(value = "/verify-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> verifyPin(@Valid @RequestBody VerifyPasswordRequest request) {
        return ApiUtil.buildResponse(usersService.verifyPassword(request), HttpStatus.OK.toString(), "Successful");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PreAuthorize("hasAuthority('SCOPE_users.onboarding.stage')")
    @GetMapping(value = "/process-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<StageResponse>> processDetails(@Pattern(regexp = AppConstants.EMAIL_REGEX_PATTERN, message = "Enter a valid email") @RequestParam(name = "email") String email) {
        return ApiUtil.buildResponse(usersService.processDetails(email), HttpStatus.OK.toString(), "Successful");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PreAuthorize("hasRole('ROLE_1049')")
    @PostMapping(value = "/create-spouse", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UpdateResponse>> createSpouse(@Valid @RequestBody CreateSpouseRequest request) {
        return ApiUtil.buildResponse(usersService.createSpouse(request), HttpStatus.OK.toString(), "Successful");
    }
}
