package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface UserOnboardingRepository extends BaseRepository<UserOnboarding, Long> {

    @Query("SELECT rr.id, rr.requirement_name, uo.completed, rr.mandatory, rr.display_name, rr.requirement_type FROM user_onboarding uo " +
            "LEFT JOIN requirements rr ON rr.id = uo.requirement_id WHERE uo.user_id = :userId AND rr.status = :status AND rr.requirement_type = :type")
    List<UserOnboardingResponse> findAllUserOnboardingsByUserId(Long userId, Integer status, Integer type);

    @Modifying
    @Transactional
    @Query("UPDATE user_onboarding SET completed = TRUE WHERE user_id = :userId AND requirement_id = :requirementId ")
    int completeUserOnboarding(Long userId, Long requirementId);

    boolean existsByUserIdAndRequirementIdAndCompleted(Long userId, Long requirementId, boolean completed);

    @Query("SELECT COUNT(uo.id) = SUM(CASE WHEN completed = TRUE THEN 1 ELSE 0 END) FROM user_onboarding uo LEFT JOIN requirements r on r.id = uo.requirement_id WHERE user_id = :userId AND r.mandatory = TRUE")
    boolean allRequirementsSubmitted(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_onboarding SET status = :value, completed = :completed WHERE user_id = :userId AND requirement_id = :requirementId ")
    void updateUserOnboardingStatus(Long userId, Long requirementId, Integer value, boolean completed);


    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM user_onboarding WHERE user_id = :userId " +
            "AND requirement_id = :requirementId AND completed = :completed ")
    boolean userOnboardingCompleted(Long userId, Long requirementId, boolean completed);

    UserOnboarding findAllUserOnboardingsByUserIdAndRequirementId(Long userId, Long requirementId);
}
