package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.UserFeature;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserFeatureRepository extends BaseRepository<UserFeature, Long> {
    boolean existsByUserIdAndFeatureId(Long userId, Long featureId);

    @Modifying
    @Transactional
    @Query("UPDATE user_feature SET completed = TRUE WHERE user_id = :userId AND feature_id = :featureId ")
    void updateUserFeature(Long userId, Long featureId);
}
