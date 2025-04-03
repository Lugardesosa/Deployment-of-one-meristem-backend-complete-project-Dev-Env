package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.UserFeature;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserFeatureRepository extends BaseRepository<UserFeature, Long> {
    boolean existsByUserIdAndFeatureId(@NotNull(message = "Mot null.") Long userId, @NotNull(message = "Mot null.") Long featureId);
}
