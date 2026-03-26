package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.models.IndividualAccount;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface IndividualAccountRepository extends BaseRepository<IndividualAccount, Long> {

    @Cacheable(value = AppConstants.INDIVIDUAL_ACCOUNT_CACHE_NAME, key = "#a0", unless = "#result == null")
    @Query("SELECT customer_id FROM individual_account WHERE user_id = :userId")
    String getCustomerIdByUserId(Long userId);
}
