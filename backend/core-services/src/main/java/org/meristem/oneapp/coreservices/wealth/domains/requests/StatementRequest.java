package org.meristem.oneapp.coreservices.wealth.domains.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatementRequest {

    @NotBlank(message = "fundId is required")
    private String fundId;

    @NotBlank(message = "startDate is required")
    private String startDate;

    @NotBlank(message = "endDate is required")
    private String endDate;
}
