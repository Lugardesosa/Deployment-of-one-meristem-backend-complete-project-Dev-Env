package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface UserOnboardingRepository extends BaseRepository<UserOnboarding, Long> {

    @Query("SELECT rr.requirement_name, uo.completed, fr.requirement_stage," +
            " fr.mandatory FROM user_onboarding uo LEFT JOIN feature_requirement fr ON fr.requirement_id = uo.requirement_id AND " +
            "fr.feature_id = uo.feature_id LEFT JOIN requirements rr ON rr.id = uo.requirement_id " +
            "WHERE uo.user_id = :userId AND uo.feature_id = :featureId ")
    List<UserOnboardingResponse> findALlUserOnboardingsByUserIdAndFeatureId(Long userId, Long featureId);
}
