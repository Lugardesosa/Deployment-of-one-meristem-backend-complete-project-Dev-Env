package org.meristem.oneapp.reportservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.reportservice.constants.AppConstants;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Base request object for pagination and sorting")
public class PageRequest {

    @Builder.Default
    @Schema(description = "Number of items per page", example = "10")
    @Max(value = 200, message = "Maximum number of items per page is 200")
    private Integer size = AppConstants.PAGE_SIZE;

    @Builder.Default
    @Schema(description = "Page number to retrieve. First page is 0, second is 1", example = "0")
    private Integer page = 0;

    @Builder.Default
    @Schema(description = "Field to sort by", example = "createdDate")
    private List<String> sortBy = Collections.singletonList("createdDate");

    @Builder.Default
    @Schema(anyOf = {Sort.Direction.class}, description = "Sort order (ASC or DESC)", example = "DESC")
    private Sort.Direction sortOrder = Sort.Direction.DESC;

    public PageRequest(int page, List<String> sortBy, Sort.Direction sortOrder) {
        this(page, AppConstants.PAGE_SIZE, sortBy, sortOrder);
    }

    public PageRequest(int page, List<String> sortBy) {
        this(page, AppConstants.PAGE_SIZE, sortBy, Sort.Direction.DESC);
    }

    public PageRequest(int page) {
        this(page, AppConstants.PAGE_SIZE, Collections.singletonList("createdDate"), Sort.Direction.DESC);
    }
}
