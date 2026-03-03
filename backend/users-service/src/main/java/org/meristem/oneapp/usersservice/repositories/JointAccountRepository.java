package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.JointAccountDetailsResponse;
import org.meristem.oneapp.usersservice.models.JointAccount;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface JointAccountRepository extends BaseRepository<JointAccount, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE users SET customer_id = :customerId WHERE account_id IN :accountId ")
    void updateAllCustomerId(String customerId, String accountId);

    List<JointAccountDetailsResponse> findAccountPartiesByUserId(Long userId);
}
