package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.responses.DependentAccountDetailsResponse;
import org.meristem.oneapp.usersservice.domains.responses.JointAccountDetailsResponse;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.models.DependentAccount;
import org.meristem.oneapp.usersservice.models.JointAccount;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DependentAccountRepository extends BaseRepository<DependentAccount, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE dependent_account SET customer_id = :customerId WHERE user_id = :userId ")
    void updateAllCustomerId(String customerId, Long userId);

    @Cacheable(value = AppConstants.DEPENDENT_ACCOUNT_CACHE_NAME, key = "#a0", unless = "#result == null")
    List<DependentAccountDetailsResponse> findAccountPartiesByUserId(Long userId);

    DependentAccount findDependentAccountByUserId(Long userId);

    @Query(value = """
          SELECT u.*,
                 up.user_id, up.image_key, up.date_of_birth, up.gender, up.state_of_origin, up.country_of_origin, up.lg_of_origin, up.marital_status, up.chn_number, up.cscs_number, up.referral_code, up.biometric_enabled, up.interest_free_investment, up.email_verified, up.tax_id, occupation, up.source_of_income, up.employer_name, up.data_sharing, up.marketing_data_sharing, up.ai_and_analytics_data_sharing,
                 da.parent_user_id, da.customer_id
          FROM users u LEFT JOIN user_profile up ON u.id = up.user_id 
          LEFT JOIN dependent_account da ON da.user_id = u.id WHERE da.parent_user_id = :parentUserId 
    """)
    List<UsersResponse.DependentAccounts> findDependentAccountsByParentUserId(Long parentUserId);
}
