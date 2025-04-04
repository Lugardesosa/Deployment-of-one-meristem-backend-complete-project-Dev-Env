package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Column;

@Builder
public record UserOnboardingResponse(@Column("id") Long requirementId, @Column("requirement_name") String requirementName, Boolean completed,
                                     @Column("requirement_stage") Integer requirementStage, Boolean mandatory) {
}
