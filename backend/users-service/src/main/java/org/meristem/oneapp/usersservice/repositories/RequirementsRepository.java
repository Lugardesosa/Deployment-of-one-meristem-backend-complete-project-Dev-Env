package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.Requirements;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RequirementsRepository extends BaseRepository<Requirements, Long> {

    @Query("SELECT id FROM requirements WHERE requirement_name = :requirementName")
    Long findIdByRequirementName(@NotNull(message = "requirementName cannot be null") String requirementName);
}
