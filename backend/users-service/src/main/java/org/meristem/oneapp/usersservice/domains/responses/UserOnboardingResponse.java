package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Column;

//@Query("SELECT rr.requirement_name AS requirementName, uo.completed, fr.requirement_stage AS requirementStage," +
@Builder
public record UserOnboardingResponse(@Column("requirement_name") String requirementName, Boolean completed,
                                     @Column("requirement_stage") Integer requirementStage, Boolean mandatory) {
}
