package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.models.Wallets;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface WalletRepository extends BaseRepository<Wallets, Long> {

    @Lock(LockMode.PESSIMISTIC_WRITE)
    Wallets findWalletsById(Long id);
}
