package org.meristem.oneapp.reportservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meristem.oneapp.reportservice.constants.AppConstants;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Base request object for pagination and sorting")
public class PageRequest {

    @Schema(description = "Number of items per page", example = "10")
    private Integer size = AppConstants.PAGE_SIZE;
    @Schema(description = "Page number to retrieve. First page is 0, second is 1", example = "0")
    private Integer page = 0;
    @Schema(description = "Field to sort by", example = "createdDate")
    private List<String> sortBy = Collections.singletonList("createdDate");
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
