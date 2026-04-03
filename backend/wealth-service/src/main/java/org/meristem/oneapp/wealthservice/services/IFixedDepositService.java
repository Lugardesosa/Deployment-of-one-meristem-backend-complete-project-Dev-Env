package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.PlacementPreviewResponse;

public interface IFixedDepositService {
    PlacementPreviewResponse preview(FixedDepositPreviewRequest request);
}