package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface UserOnboardingRepository extends BaseRepository<UserOnboarding, Long> {

    @Query("SELECT rr.id, rr.requirement_name, uo.completed, fr.requirement_stage," +
            " fr.mandatory FROM user_onboarding uo LEFT JOIN feature_requirement fr ON fr.requirement_id = uo.requirement_id AND " +
            "fr.feature_id = uo.feature_id LEFT JOIN requirements rr ON rr.id = uo.requirement_id " +
            "WHERE uo.user_id = :userId AND uo.feature_id = :featureId ")
    List<UserOnboardingResponse> findAllUserOnboardingsByUserIdAndFeatureId(Long userId, Long featureId);

    @Modifying
    @Transactional
    @Query("UPDATE user_onboarding SET completed = TRUE WHERE user_id = :userId AND feature_id = :featureId AND requirement_id = :requirementId ")
    void completeUserOnboarding(Long userId, Long featureId, Long requirementId);

    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM user_onboarding WHERE user_id = :userId " +
            "AND feature_id = :featureId AND requirement_id = :requirementId AND completed = :completed ")
    boolean existsByUserIdAndFeatureIdAndRequirementIdAndCompleted(Long userId, Long featureId, Long requirementId, boolean completed);

    @Query("SELECT COUNT(*) = SUM(CASE WHEN completed = TRUE THEN 1 ELSE 0 END) FROM user_onboarding WHERE user_id = :userId AND feature_id = :featureId ")
    boolean allRequirementsSubmitted(@NotNull Long userId, @NotNull Long featureId);
}
