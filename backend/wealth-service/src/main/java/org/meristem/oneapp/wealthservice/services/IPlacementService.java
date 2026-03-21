package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.requests.PlacementCreateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.PlacementPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.requests.PlacementTopupRequest;
import org.meristem.oneapp.wealthservice.domains.responses.*;

import java.util.List;

public interface IPlacementService {

    List<PlacementProductResponse> getPlacementProducts();

    PlacementRateResponse getPlacementRate(String productId);

    PlacementPreviewResponse previewPlacement(PlacementPreviewRequest request);

    PlacementActionResponse createPlacement(PlacementCreateRequest request);

    PlacementActionResponse topUpPlacement(PlacementTopupRequest request);

    List<PlacementSummaryResponse> getPlacementsByCustomer(String customerId);

    List<PlacementTransactionResponse> getPlacementTransactions(String fundAccountId);
}
