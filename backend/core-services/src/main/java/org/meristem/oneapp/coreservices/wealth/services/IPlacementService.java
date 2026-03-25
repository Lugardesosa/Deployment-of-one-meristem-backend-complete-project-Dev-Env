package org.meristem.oneapp.coreservices.wealth.services;

import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementCreateRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementPreviewRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementTopupRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;

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
