package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.models.Transactions;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TransactionsRepository extends BaseRepository<Transactions, Long> {
}
