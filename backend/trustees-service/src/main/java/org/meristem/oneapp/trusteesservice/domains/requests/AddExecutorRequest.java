package org.meristem.oneapp.trusteesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "AddExecutorRequest", description = "Request to add one or more executors to a will.")
public record AddExecutorRequest(

        @Schema(description = "Owner's id if created by an admin", example = "1")
        Long ownerId,

        @Schema(description = "Unique identifier of the will.", example = "42")
        @NotNull(message = "Cannot be null") Long planId,

        @Schema(description = "Enum of the will.", example = "SIMPLE_WILL", allowableValues = {"COMPREHENSIVE_WILL", "SIMPLE_WILL"})
        @NotBlank(message = "Cannot be null") String planType,

        @Schema(
                description = "List of executor entries to be added.",
                example = "[{\"willExecutorName\":\"Jane Doe\",\"willExecutorAddress\":\"221B Baker Street, London\"}]",
                anyOf = {ExecutorRequest.class}
        )
        List<@Valid ExecutorRequest> executorRequests
) {

    @Schema(name = "ExecutorRequest", description = "Details of a single executor.")
    public record ExecutorRequest(
            @NotBlank(message = "Cannot be null")
            @Size(min = 1, max = 300)
            @Schema(description = "Executor's full name.", example = "Jane Doe")
            String willExecutorName,

            @Size(min = 1, max = 300)
            @NotBlank(message = "Cannot be null")
            @Schema(description = "Executor's residential address.", example = "10 Downing Street, London, UK")
            String willExecutorAddress
    ) {}
}
