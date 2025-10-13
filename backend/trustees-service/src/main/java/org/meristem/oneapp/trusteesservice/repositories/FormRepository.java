package org.meristem.oneapp.trusteesservice.repositories;


import org.meristem.oneapp.trusteesservice.domains.responses.FormResponse;
import org.meristem.oneapp.trusteesservice.models.Forms;
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

    @Query("SELECT f.* FROM forms f WHERE f.internal_order = :internalOrder")
    List<FormResponse.FormData> findFormsByInternalOrder(Integer internalOrder);
}
