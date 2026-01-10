package org.meristem.oneapp.walletservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.domains.requests.WemaAccountQueryRequest;
import org.meristem.oneapp.walletservice.domains.responses.VirtualAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.WemaAccountQueryResponse;

import java.util.List;

public interface IVirtualAccountService {
    void createVirtualAccounts(KycCompletedDto record);
    WemaAccountQueryResponse queryWemaAccount(@Valid WemaAccountQueryRequest request);
    List<VirtualAccountResponse> getAccounts();
}
