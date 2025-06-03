package org.meristem.oneapp.usersservice.domains.requests;

import com.obs.services.model.HttpMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Request containing the method and file name for generating a signed URL")
public record SignedUrlRequest(
        @Schema(example = "GET", description = "HTTP method to be used for the signed URL")
        @NotNull(message = "cannot be null") HttpMethodEnum method,
        @Schema(example = "document.pdf", description = "Name of the file for which the signed URL is generated")
        @NotBlank(message = "Cannot be blank") String fileName) {
}
