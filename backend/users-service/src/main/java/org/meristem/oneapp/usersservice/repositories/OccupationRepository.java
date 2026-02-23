package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.OccupationResponse;
import org.meristem.oneapp.usersservice.models.Occupation;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface OccupationRepository extends BaseRepository<Occupation, Long> {


    @Query("SELECT id, name FROM occupation")
    List<OccupationResponse.Occupation> findAllOccupations();
}
