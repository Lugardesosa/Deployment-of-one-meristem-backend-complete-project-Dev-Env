package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.UserPin;
import org.springframework.data.domain.Limit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserPinRepository extends BaseRepository<UserPin, Long> {
    Optional<UserPin> findByUserId(Long userId);

    List<UserPin> findAllByLockUntilBefore(LocalDateTime lockUntilBefore, Limit limit);

    List<UserPin> findAllByStatusAndLockUntilBefore(Integer status, LocalDateTime lockUntilBefore, Limit limit);
}
