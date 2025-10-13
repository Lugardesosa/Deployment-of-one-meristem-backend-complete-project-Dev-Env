package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.models.ProvidusBankCodes;

public interface ProvidusBankCodesRepository extends BaseRepository<ProvidusBankCodes, Long> {
    ProvidusBankCodes findByBankCode(String bankCode);
}
