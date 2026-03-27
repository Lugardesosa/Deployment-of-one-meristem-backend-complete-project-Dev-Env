package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.models.UserCustomerIds;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface UserCustomerIdsRepository extends BaseRepository<UserCustomerIds, Long> {

    @Cacheable(value = AppConstants.USER_CUSTOMER_ID_CACHE_NAME, key = "#userId", unless = "#result == null")
    @Query("SELECT customer_id FROM user_customer_id WHERE user_id = :userId")
    List<String> getAllCustomerIdsByUserId(Long userId);
}
