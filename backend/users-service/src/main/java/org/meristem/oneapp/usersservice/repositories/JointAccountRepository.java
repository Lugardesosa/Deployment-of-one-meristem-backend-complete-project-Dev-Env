package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.responses.JointAccountDetailsResponse;
import org.meristem.oneapp.usersservice.models.JointAccount;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface JointAccountRepository extends BaseRepository<JointAccount, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE joint_account SET customer_id = :customerId WHERE account_id = :accountId ")
    void updateAllCustomerId(String customerId, String accountId);

    @Cacheable(value = AppConstants.JOINT_ACCOUNT_CACHE_NAME, key = "#a0", unless = "#result == null")
    List<JointAccountDetailsResponse> findAccountPartiesByUserId(Long userId);

    @Query("SELECT COUNT(customer_id) FROM joint_account WHERE account_id = :accountId ")
    Integer findCustomerIdByAccountId(String accountId);

    @Cacheable(value = AppConstants.JOINT_ACCOUNT_CUSTOMER_ID_CACHE_NAME, key = "#a0", unless = "#result == null")
    @Query("SELECT customer_id FROM joint_account WHERE user_id = :loggedInUserId")
    String getCustomerIdByUserId(Long loggedInUserId);
}
