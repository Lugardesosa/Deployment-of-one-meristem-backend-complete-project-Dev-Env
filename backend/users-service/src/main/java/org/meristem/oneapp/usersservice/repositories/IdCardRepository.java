package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.IdCard;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface IdCardRepository extends BaseRepository<IdCard, Long> {
    boolean existsByIdValue(String idValue);
}
