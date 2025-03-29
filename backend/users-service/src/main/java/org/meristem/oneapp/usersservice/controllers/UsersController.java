package org.meristem.oneapp.usersservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.UserResponse;
import org.meristem.oneapp.usersservice.services.UsersService;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "base")
public class UsersController {

    private final UsersService usersService;


    @Operation(summary = "Creates a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserRequest.class))
            })
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Object>> createUser(@RequestBody @Valid CreateUserRequest userRequest) {
         return ApiUtil.buildResponse(usersService.createUser(userRequest), HttpStatus.CREATED.toString(), "Created successfully.");
    }

    @Operation(summary = "Gets users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the user.")
    })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UserResponse>> getUser(@RequestParam(name = "email") String email) {
        return ApiUtil.buildResponse(usersService.getUser(email), HttpStatus.CREATED.toString(), "Successful.");
    }
}
