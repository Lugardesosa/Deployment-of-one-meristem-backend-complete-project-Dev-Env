package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.SourceOfIncomeResponse;
import org.meristem.oneapp.usersservice.models.SourceOfIncome;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface SourceOfIncomeRepository extends BaseRepository<SourceOfIncome, Long> {

    @Query("SELECT id, name FROM source_of_income")
    List<SourceOfIncomeResponse.SoI> findAllSourceOfIncome();
}
