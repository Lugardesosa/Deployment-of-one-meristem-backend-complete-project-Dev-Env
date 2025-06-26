package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.models.VirtualAccounts;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface VirtualAccountRepository extends BaseRepository<VirtualAccounts, Long> {

    @Query("SELECT account_name from virtual_accounts account_number = :accountNumber AND bank_code = :bankCode")
    String getFullNameByAccountNumber(String accountNumber, String bankCode);

    @Lock(LockMode.PESSIMISTIC_WRITE)
    Optional<VirtualAccounts> findByAccountNumberAndBankCode(String accountNumber, String bankCode);

    @Query("SELECT account_name from virtual_accounts account_number = :accountNumber AND bank_code = :bankCode")
    String getFullNameByAccountNumberAndBankCode(String s, String bankCode);
}
