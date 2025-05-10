package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.NextOfKin;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface NextOfKinRepository extends BaseRepository<NextOfKin, Long> {
    boolean existsByUserId(@NotNull(message = "User id cannot be null") Long userId);
}
