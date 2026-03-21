package org.meristem.oneapp.usersservice.domains.requests;

import com.obs.services.model.HttpMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.SignedUrlType;

@Builder
@Schema(description = "Request containing the method and file name for generating a signed URL")
public record SignedUrlRequest(
        @Schema(anyOf = {HttpMethodEnum.class}, example = "GET", description = "HTTP method to be used for the signed URL. Use PUT for uploading and GET to get the file")
        @NotNull(message = "cannot be null") HttpMethodEnum method,
        @Schema(example = "document.pdf", description = "Name of the file for which the signed URL is generated")
        @NotBlank(message = "Cannot be blank") String fileName,
        @Schema(anyOf = {SignedUrlType.class}, example = "IMAGE", description = "Pass IMAGE if file is an image and DOCUMENT if file is a document")
        @NotNull(message = "cannot be null") SignedUrlType type,
        @Schema(example = "image/png", description = "Pass the content type of the document if the signed url is for uploading, that is if the method is PUT") String contentType
) {
}
