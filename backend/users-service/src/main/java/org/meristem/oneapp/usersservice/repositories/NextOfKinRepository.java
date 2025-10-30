package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.NextOfKin;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface NextOfKinRepository extends BaseRepository<NextOfKin, Long> {
    boolean existsByUserId(Long userId);

    NextOfKin findByUserId(Long loggedInUserId);
}
