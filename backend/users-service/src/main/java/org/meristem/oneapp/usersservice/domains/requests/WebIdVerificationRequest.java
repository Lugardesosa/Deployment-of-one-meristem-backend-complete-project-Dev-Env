package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.kafka.common.metrics.internals.IntGaugeSuite;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class WebIdVerificationRequest {

    //    private @NotBlank(message = "Cannot be blank") String jobId;
    @Schema(description = "pass true if it is a secondary user")
    private boolean secondary;
    @Schema(description = "pass true if it is an existing meristem user")
    private boolean existingUser;
    private @NotBlank(message = "Cannot be blank") String bvn;
    private @NotEmpty(message = "Cannot be empty") List<@Valid Image> images;
    private PartnerParams partnerParams;

    private @NotNull(message = "investmentRequirementId cannot be null") Long investmentRequirementId;


    public record Image(@NotBlank(message = "Cannot be blank") String image,
                        @NotNull(message = "Cannot be null") Integer imageTypeId) {
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class PartnerParams {

        private String sandboxResult;
        private String userId;
        @NotNull(message = "Cannot be null")
        private Integer jobType;
    }
}
