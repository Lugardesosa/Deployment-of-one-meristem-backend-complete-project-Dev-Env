package org.meristem.oneapp.coreservices.wealth.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementCreateRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementPreviewRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.PlacementTopupRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;
import org.meristem.oneapp.coreservices.wealth.integrations.MiddleWareClient;
import org.meristem.oneapp.coreservices.wealth.mappers.MiddlewareMapper;
import org.meristem.oneapp.coreservices.wealth.services.IPlacementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlacementService implements IPlacementService {

    private final MiddleWareClient middleWareClient;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;

    @Override
    public List<PlacementProductResponse> getPlacementProducts() {
        return middlewareMapper.middlewarePlacementProductResponseToPlacementProductResponse( middleWareClient.getPlacementProducts().data());
    }

    @Override
    public PlacementRateResponse getPlacementRate(String productId) {
        return middlewareMapper.middlewarePlacementRateResponseToPlacementRateResponse(middleWareClient.getPlacementRate(productId).data());
    }

    @Override
    public PlacementPreviewResponse previewPlacement(PlacementPreviewRequest request) {
        return middlewareMapper.middlewarePlacementPreviewResponseToPlacementPreviewResponse(
                middleWareClient.previewPlacement(middlewareMapper.placementPreviewRequestToMiddlewarePlacementPreviewRequest(request)).data()
        );
    }

    @Override
    public PlacementActionResponse createPlacement(PlacementCreateRequest request) {
        return middlewareMapper.middlewarePlacementActionResponseToPlacementActionResponse(
                middleWareClient.createPlacement(middlewareMapper.placementCreateRequestToMiddlewarePlacementCreateRequest(request)).data()
        );
    }

    @Override
    public PlacementActionResponse topUpPlacement(PlacementTopupRequest request) {
        return middlewareMapper.middlewarePlacementActionResponseToPlacementActionResponse(
                middleWareClient.topUpPlacement(middlewareMapper.placementTopupRequestToMiddlewarePlacementTopupRequest(request)).data()
        );
    }

    @Override
    public List<PlacementSummaryResponse> getPlacementsByCustomer(String customerId) {
        return middlewareMapper.middlewarePlacementSummaryResponseToPlacementSummaryResponse(middleWareClient.getPlacementsByCustomer(customerId).data());
    }

    @Override
    public List<PlacementTransactionResponse> getPlacementTransactions(String fundAccountId) {
        return middlewareMapper.middlewarePlacementTransactionResponseToPlacementTransactionResponse(middleWareClient.getPlacementTransactions(fundAccountId).data());
    }
}
