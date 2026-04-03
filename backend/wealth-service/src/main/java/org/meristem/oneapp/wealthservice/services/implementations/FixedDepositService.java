package org.meristem.oneapp.wealthservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.PlacementPreviewResponse;
import org.meristem.oneapp.wealthservice.integrations.MiddleWareClient;
import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementPreviewRequest;
import org.meristem.oneapp.wealthservice.mappers.MiddlewareMapper;
import org.meristem.oneapp.wealthservice.services.IFixedDepositService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FixedDepositService implements IFixedDepositService {

    private final MiddleWareClient middleWareClient;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;

    @Override
    public PlacementPreviewResponse preview(FixedDepositPreviewRequest request) {
        MiddlewarePlacementPreviewRequest middlewareRequest = new MiddlewarePlacementPreviewRequest(
                request.fundId(),
                request.productId(),
                request.effectiveDate(),
                request.amount(),
                request.tenorDays(),
                request.customerId()
        );
        return middlewareMapper.middlewarePlacementPreviewResponseToPlacementPreviewResponse(
                middleWareClient.previewPlacement(middlewareRequest).data()
        );
    }
}