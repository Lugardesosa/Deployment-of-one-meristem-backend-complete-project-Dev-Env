package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.domains.responses.VirtualAccountResponse;
import org.meristem.oneapp.walletservice.models.VirtualAccounts;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VirtualAccountRepository extends BaseRepository<VirtualAccounts, Long> {

    @Query("SELECT account_name from virtual_accounts account_number = :accountNumber AND bank_code = :bankCode")
    String getFullNameByAccountNumber(String accountNumber, String bankCode);

    @Lock(LockMode.PESSIMISTIC_WRITE)
    Optional<VirtualAccounts> findByAccountNumberAndBankCode(String accountNumber, String bankCode);

    @Query("SELECT account_name from virtual_accounts account_number = :accountNumber AND bank_code = :bankCode")
    String getFullNameByAccountNumberAndBankCode(String s, String bankCode);

    @Query("SELECT v.account_number, v.bank_name FROM virtual_accounts v LEFT JOIN wallets w ON w.id = v.wallet_id WHERE user_id = :userId ")
    List<VirtualAccountResponse> findAccountNumberAndBankNameByUserId(Long userId);

    Optional<VirtualAccounts> findByBankCodeAndWalletId(String bankCode, Long walletId);
}
