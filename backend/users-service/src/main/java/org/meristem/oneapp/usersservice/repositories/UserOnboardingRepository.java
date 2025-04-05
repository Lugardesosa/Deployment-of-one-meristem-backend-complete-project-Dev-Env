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

    @Query("SELECT rr.id, rr.requirement_name, uo.completed, rr.mandatory, rr.display_name FROM user_onboarding uo " +
            "LEFT JOIN requirements rr ON rr.id = uo.requirement_id WHERE uo.user_id = :userId AND rr.status = :status ")
    List<UserOnboardingResponse> findAllUserOnboardingsByUserId(Long userId, Integer status);

    @Modifying
    @Transactional
    @Query("UPDATE user_onboarding SET completed = TRUE WHERE user_id = :userId AND requirement_id = :requirementId ")
    int completeUserOnboarding(Long userId, Long requirementId);

    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM user_onboarding WHERE user_id = :userId " +
            "AND requirement_id = :requirementId AND completed = :completed ")
    boolean existsByUserIdAndRequirementIdAndCompleted(Long userId, Long requirementId, boolean completed);

    @Query("SELECT COUNT(*) = SUM(CASE WHEN completed = TRUE THEN 1 ELSE 0 END) FROM user_onboarding WHERE user_id = :userId ")
    boolean allRequirementsSubmitted(@NotNull Long userId);
}
