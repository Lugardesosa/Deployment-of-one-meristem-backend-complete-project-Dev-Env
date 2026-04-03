package org.meristem.oneapp.wealthservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.wealthservice.domains.requests.*;
import org.meristem.oneapp.wealthservice.domains.responses.*;
import org.meristem.oneapp.wealthservice.integrations.requests.*;
import org.meristem.oneapp.wealthservice.integrations.responses.*;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MiddlewareMapper {

    MiddlewareMapper INSTANCE = Mappers.getMapper(MiddlewareMapper.class);

    MiddlewareFundRedemptionRequest fundRedemptionRequestToMiddlewareFundRedemptionRequest(FundRedemptionRequest request);

    MiddlewareFundSubscriptionRequest fundSubscriptionRequestToMiddlewareFundSubscriptionRequest(FundSubscriptionRequest request);

    MiddlewarePlacementCreateRequest placementCreateRequestToMiddlewarePlacementCreateRequest(PlacementCreateRequest request);

    default MiddlewarePlacementPreviewRequest placementPreviewRequestToMiddlewarePlacementPreviewRequest(PlacementPreviewRequest request) {
        return null;
    }

    MiddlewarePlacementTopupRequest placementTopupRequestToMiddlewarePlacementTopupRequest(PlacementTopupRequest request);

    MiddlewarePortfolioStatementRequest portfolioStatementRequestToMiddlewarePortfolioStatementRequest(PortfolioStatementRequest request);

    MiddlewareTBillPreviewRequest tBillPreviewRequestToMiddlewareTBillPreviewRequest(TBillPreviewRequest request);

    MiddlewareTBillPurchaseRequest tBillPurchaseRequestToMiddlewareTBillPurchaseRequest(TBillPurchaseRequest request);

    MiddlewareTBillSellPreviewRequest tBillSellPreviewRequestToMiddlewareTBillSellPreviewRequest(TBillSellPreviewRequest request);

    MiddlewareTBillSellRequest tBillSellRequestToMiddlewareTBillSellRequest(TBillSellRequest request);

    List<ActiveTBillResponse> middlewareActiveTBillResponseToActiveTBillResponse(List<MiddlewareActiveTBillResponse> response);

    List<FundAccountsResponse> middlewareFundAccountsResponseToFundAccountsResponse(List<MiddlewareFundAccountsResponse> response);

    FundDetailsResponse middlewareFundDetailsResponseToFundDetailsResponse(MiddlewareFundDetailsResponse response);

    List<FundHistoryResponse> middlewareFundHistoryResponseToFundHistoryResponse(List<MiddlewareFundHistoryResponse> response);

    FundRedemptionResponse middlewareFundRedemptionResponseToFundRedemptionResponse(MiddlewareFundRedemptionResponse response);

    List<FundResponse> middlewareFundResponseToFundResponse(List<MiddlewareFundResponse> response);

    List<FundStatementResponse> middlewareFundStatementResponseToFundStatementResponse(List<MiddlewareFundStatementResponse> response);

    FundSubscriptionResponse middlewareFundSubscriptionResponseToFundSubscriptionResponse(MiddlewareFundSubscriptionResponse response);

    List<InvestmentProductResponse> middlewareInvestmentProductResponseToInvestmentProductResponse(List<MiddlewareInvestmentProductResponse> response);

    PlacementActionResponse middlewarePlacementActionResponseToPlacementActionResponse(MiddlewarePlacementActionResponse response);

    PlacementPreviewResponse middlewarePlacementPreviewResponseToPlacementPreviewResponse(MiddlewarePlacementPreviewResponse response);

    List<PlacementProductResponse> middlewarePlacementProductResponseToPlacementProductResponse(List<MiddlewarePlacementProductResponse> response);

    PlacementRateResponse middlewarePlacementRateResponseToPlacementRateResponse(MiddlewarePlacementRateResponse response);

    List<PlacementSummaryResponse> middlewarePlacementSummaryResponseToPlacementSummaryResponse(List<MiddlewarePlacementSummaryResponse> response);

    List<PlacementTransactionResponse> middlewarePlacementTransactionResponseToPlacementTransactionResponse(List<MiddlewarePlacementTransactionResponse> response);

    List<PortfolioActivityResponse> middlewarePortfolioActivityResponseToPortfolioActivityResponse(List<MiddlewarePortfolioActivityResponse> response);

    List<PortfolioAssetClassBreakdownResponse> middlewarePortfolioAssetToPortfolioAssetClassBreakdownResponse(List<MiddlewarePortfolioAssetClassBreakdownResponse> response);

    List<PortfolioResponse> middlewarePortfolioResponseToPortfolioResponse(List<MiddlewarePortfolioResponse> response);

    PortfolioStatementResponse middlewarePortfolioStatementToPortfolioStatementResponse(MiddlewarePortfolioStatementResponse response);

    List<RateQuoteResponse> middlewareRateQuoteResponseToRateQuoteResponse(List<MiddlewareRateQuoteResponse> response);

    TBillPreviewResponse middlewareTBillPreviewResponseToTBillPreviewResponse(MiddlewareTBillPreviewResponse response);

    List<TBillProductResponse> middlewareTBillProductResponseToTBillProductResponse(List<MiddlewareTBillProductResponse> response);

    TBillPurchaseResponse middlewareTBillPurchaseResponseToTBillPurchaseResponse(MiddlewareTBillPurchaseResponse response);

    TBillRateResponse middlewareTBillRateResponseToTBillRateResponse(MiddlewareTBillRateResponse response);

    TBillSellPreviewResponse middlewareTBillSellPreviewToTBillSellPreviewResponse(MiddlewareTBillSellPreviewResponse response);

    TBillSellResponse middlewareTBillSellResponseToTBillSellResponse(MiddlewareTBillSellResponse response);
}
