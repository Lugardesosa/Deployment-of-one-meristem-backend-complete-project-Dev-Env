package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.usersservice.models.Avatars;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AvatarsRepository extends BaseRepository<Avatars, Long> {
    boolean existsByUrl(@Size(max = 500, message = "cannot be more than 300") @NotBlank(message = "cannot be null") String url);
}
