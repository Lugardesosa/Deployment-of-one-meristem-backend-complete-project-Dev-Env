package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface UserOnboardingRepository extends BaseRepository<UserOnboarding, Long> {

    @Query("""
            SELECT ir.id, rr.requirement_name, uo.completed, ir.mandatory, rr.display_name, ir.requirement_type, uo.status 
            FROM user_onboarding uo LEFT JOIN investment_requirement ir ON ir.id = uo.investment_requirement_id 
            LEFT JOIN requirements rr on ir.requirement_id = rr.id LEFT JOIN investment_instruments ii ON ii.id = ir.investment_id
            WHERE uo.user_id = :userId AND rr.status = :status AND ir.requirement_type = :type AND ii.id = :productId""")
    List<UserOnboardingResponse> findAllUserOnboardingsByUserId(Long userId, Integer status, Integer type, Long productId);

    @Modifying
    @Transactional
    @Query("UPDATE user_onboarding SET completed = TRUE WHERE user_id = :userId AND investment_requirement_id = :investmentRequirementId ")
    int completeUserOnboarding(Long userId, Long investmentRequirementId);

    boolean existsByUserIdAndInvestmentRequirementIdAndCompleted(Long userId, Long investmentRequirementId, boolean completed);

    @Query("SELECT COUNT(uo.id) = SUM(CASE WHEN uo.completed = TRUE THEN 1 ELSE 0 END) FROM user_onboarding uo LEFT JOIN investment_requirement ir on ir.id = uo.investment_requirement_id WHERE uo.user_id = :userId AND ir.mandatory = TRUE AND ir.investment_id = :investmentId")
    boolean allRequirementsSubmitted(Long userId, Long investmentId);

    @Modifying
    @Transactional
    @Query("UPDATE user_onboarding SET status = :value, completed = :completed, note = :note WHERE user_id = :userId AND investment_requirement_id = :investmentRequirementId ")
    void updateUserOnboardingStatus(Long userId, Long investmentRequirementId, Integer value, String note, boolean completed);

    @Modifying
    @Transactional
    @Query("UPDATE user_onboarding SET status = :value, completed = :completed, note = :note WHERE user_id = :userId AND investment_requirement_id IN (:investmentRequirementId) ")
    void updateAllUserOnboardingStatus(Long userId, List<Long> investmentRequirementId, Integer value, String note, boolean completed);


    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM user_onboarding WHERE user_id = :userId " +
            "AND requirement_id = :requirementId AND completed = :completed ")
    boolean userOnboardingCompleted(Long userId, Long requirementId, boolean completed);

    UserOnboarding findAllUserOnboardingsByUserIdAndInvestmentRequirementId(Long userId, Long requirementId);
}
