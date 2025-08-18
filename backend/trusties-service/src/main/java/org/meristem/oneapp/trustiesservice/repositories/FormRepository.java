package org.meristem.oneapp.trustiesservice.repositories;


import org.meristem.oneapp.trustiesservice.domains.responses.FormResponse;
import org.meristem.oneapp.trustiesservice.models.Forms;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface FormRepository extends BaseRepository<Forms, Long> {

    @Query("SELECT id FROM forms WHERE id IN (:longs) ")
    List<Long> findIdsInIds(Set<Long> longs);


    @Query("SELECT f.* FROM forms f WHERE f.form_position = :formPosition")
    List<FormResponse.FormData> findFormsByFormPosition(Integer formPosition);
}
