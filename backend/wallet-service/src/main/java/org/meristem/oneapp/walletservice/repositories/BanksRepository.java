package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.domains.responses.BankCodeResponse;
import org.meristem.oneapp.walletservice.models.Banks;

import java.util.List;
import java.util.Optional;

public interface BanksRepository extends BaseRepository<Banks, Long> {
    Optional<Banks> findBanksByProviderCodeAndBankCode(String providerCode, String bankCode);

    List<BankCodeResponse> findBanksByProviderCode(String providerCode);
}
