package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Requirements;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RequirementsRepository extends BaseRepository<Requirements, Long> {

    @Query("SELECT id FROM requirements WHERE requirement_name = :requirementName")
    Long findIdByRequirementName(String requirementName);

    @Query("SELECT id FROM requirements WHERE status = :status ")
    Iterable<Long> findAllByStatus(Integer status);

    Optional<Requirements> findByIdAndStatus(Long id, Integer status);

    Requirements findByRequirementNameAndStatus(String requirementName, Integer status);

    Requirements findRequirementsByRequirementName(String requirementName);
}
