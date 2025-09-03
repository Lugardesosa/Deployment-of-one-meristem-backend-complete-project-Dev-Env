package org.meristem.oneapp.reportservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.reportservice.constants.ApiConstants;
import org.meristem.oneapp.reportservice.domains.enums.ActivityLogAction;
import org.meristem.oneapp.reportservice.domains.requests.ActivityLogRequest;
import org.meristem.oneapp.reportservice.domains.responses.ActivityLogResponse;
import org.meristem.oneapp.reportservice.domains.responses.AppResponse;
import org.meristem.oneapp.reportservice.services.ActivityLogService;
import org.meristem.oneapp.reportservice.utils.ApiUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(ApiConstants.CONTEXT_PATH + "activity-logs")
@RequiredArgsConstructor
@RestController
@Tag(name = "Activity Log Controller", description = "Manages activity log query from the report microservice")
public class ActivityLogController {

    private final ActivityLogService activityLogService;


    @Operation(summary = "Gets user's transactions", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gets transactions",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityLogResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request - The request could not be processed")
    })
    @PreAuthorize("hasRole('ROLE_users.activity_log.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Page<ActivityLogResponse>>> getActivityLog(ActivityLogRequest request) {
        return ApiUtil.buildResponse(activityLogService.getActivityLogs(request), HttpStatus.OK.toString(), "Successful");
    }
}
