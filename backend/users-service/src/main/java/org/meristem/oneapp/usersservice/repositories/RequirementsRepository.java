package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.Requirements;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RequirementsRepository extends BaseRepository<Requirements, Long> {

    @Query("SELECT id FROM requirements WHERE requirement_name = :requirementName")
    Long findIdByRequirementName(@NotNull(message = "requirementName cannot be null") String requirementName);

    @Query("SELECT id FROM requirements WHERE status = :status ")
    Iterable<Long> findAllByStatus(@NotNull(message = "Cannot be null") Integer status);

    Optional<Requirements> findByIdAndStatus(@NotNull(message = "id cannot be null") Long id, @NotNull(message = "Cannot be null") Integer status);
}
