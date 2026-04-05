package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.models.Wallets;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Transactional(readOnly = true)
public interface WalletRepository extends BaseRepository<Wallets, Long> {

    Optional<Wallets> findWalletsByWalletId(String walletId);
}
