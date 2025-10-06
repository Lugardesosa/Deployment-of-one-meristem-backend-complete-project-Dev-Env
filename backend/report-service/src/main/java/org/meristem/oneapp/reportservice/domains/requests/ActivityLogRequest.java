package org.meristem.oneapp.reportservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.reportservice.domains.enums.ActivityLogAction;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Request object for filtering activity log records")
public class ActivityLogRequest extends PageRequest {

    @Schema(anyOf = {ActivityLogAction.class}, description = "Action performed that produced the activity log entry", example = "CREATED")
    private ActivityLogAction action;

    @Schema(description = "Unique identifier of the activity being queried", example = "123")
    private Long activityId;

    @Schema(description = "Name of the application that generated the activity", example = "trustees-service")
    private String application;

    @Schema(description = "Identifier of the user or system that performed the action", example = "jane.doe@meristemng.com")
    private String actor;

    @Schema(description = "Name of the domain entity affected by the action", example = "Transactions")
    private String entity;

}
