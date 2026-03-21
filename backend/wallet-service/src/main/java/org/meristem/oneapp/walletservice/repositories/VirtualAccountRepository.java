package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.models.VirtualAccounts;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface VirtualAccountRepository extends BaseRepository<VirtualAccounts, Long> {


}
