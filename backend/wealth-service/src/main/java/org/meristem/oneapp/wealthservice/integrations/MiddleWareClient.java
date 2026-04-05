package org.meristem.oneapp.wealthservice.integrations;

import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementCreateFixedDepositRequest;
import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementLiquidateRequest;
import org.meristem.oneapp.wealthservice.integrations.requests.*;
import org.meristem.oneapp.wealthservice.integrations.responses.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface MiddleWareClient {

    @GetExchange("/investments/funds")
    MiddlewareAppResponse<List<MiddlewareFundResponse>> getAllFundTypes();

    @GetExchange("/investments/funds/{fundId}")
    MiddlewareAppResponse<MiddlewareFundDetailsResponse> getFundById(@PathVariable String fundId);

    @PostExchange("/investments/funds/{fundId}/subscribe")
    MiddlewareAppResponse<MiddlewareFundSubscriptionResponse> subscribeToFund(@PathVariable String fundId,
                                                          @RequestBody MiddlewareFundSubscriptionRequest request);

    @PostExchange("/investments/funds/{fundId}/redeem")
    MiddlewareAppResponse<MiddlewareFundRedemptionResponse> redeemFund(@PathVariable String fundId,
                                                   @RequestBody MiddlewareFundRedemptionRequest request);

    @GetExchange("/investments/funds/accounts/{customerId}")
    MiddlewareAppResponse<List<MiddlewareFundAccountsResponse>> getFundAccounts(@PathVariable String customerId);

    @GetExchange("/investments/funds/portfolio/{customerId}")
    MiddlewareAppResponse<List<MiddlewarePortfolioResponse>> getPortfolio(@PathVariable String customerId);

    @GetExchange("/investments/funds/history/{fundId}")
    MiddlewareAppResponse<List<MiddlewareFundHistoryResponse>> getFundHistory(@PathVariable String fundId);

    @GetExchange("/investments/funds/statements")
    MiddlewareAppResponse<List<MiddlewareFundStatementResponse>> getFundsStatements();

    @GetExchange("/investments/products")
    MiddlewareInvestmentProductListResponse getInvestmentProducts();

    @GetExchange("/investments/placements/products")
    MiddlewareAppResponse<List<MiddlewarePlacementProductResponse>> getPlacementProducts();

    @GetExchange("/investments/placements/rates/{productId}")
    MiddlewareAppResponse<MiddlewarePlacementRateResponse> getPlacementRate(@PathVariable String productId);

    @PostExchange("/investments/placements/preview")
    MiddlewareAppResponse<MiddlewarePlacementPreviewResponse> previewPlacement(@RequestBody MiddlewarePlacementPreviewRequest request);

    @PostExchange("/investments/placements/create")
    MiddlewareAppResponse<MiddlewarePlacementActionResponse> createPlacement(@RequestBody MiddlewarePlacementCreateRequest request);

    @PostExchange("/investments/placements/topup")
    MiddlewareAppResponse<MiddlewarePlacementActionResponse> topUpPlacement(@RequestBody MiddlewarePlacementTopupRequest request);

    @GetExchange("/investments/placements/{customerId}")
    MiddlewareAppResponse<List<MiddlewarePlacementSummaryResponse>> getPlacementsByCustomer(@PathVariable String customerId);

    @GetExchange("/investments/placements/transactions/{fundAccountId}")
    MiddlewareAppResponse<List<MiddlewarePlacementTransactionResponse>> getPlacementTransactions(@PathVariable String fundAccountId);

    @GetExchange("/investments/rates/today")
    MiddlewareAppResponse<List<MiddlewareRateQuoteResponse>> getTodayRates();

    @GetExchange("/investments/tbills/products")
    MiddlewareAppResponse<List<MiddlewareTBillProductResponse>> getTBillProducts();

    @GetExchange("/investments/tbills/rates/{instrumentId}")
    MiddlewareAppResponse<MiddlewareTBillRateResponse> getTBillRate(@PathVariable String instrumentId);

    @PostExchange("/investments/tbills/preview")
    MiddlewareAppResponse<MiddlewareTBillPreviewResponse> previewTBillPurchase(@RequestBody MiddlewareTBillPreviewRequest request);

    @PostExchange("/investments/tbills/purchase")
    MiddlewareAppResponse<MiddlewareTBillPurchaseResponse> purchaseTBill(@RequestBody MiddlewareTBillPurchaseRequest request);

    @GetExchange("/investments/tbills/active/{customerId}")
    MiddlewareAppResponse<List<MiddlewareActiveTBillResponse>> getActiveTBills(@PathVariable String customerId);

    @PostExchange("/investments/tbills/sell/preview")
    MiddlewareAppResponse<MiddlewareTBillSellPreviewResponse> previewTBillSale(@RequestBody MiddlewareTBillSellPreviewRequest request);

    @PostExchange("/investments/tbills/sell")
    MiddlewareAppResponse<MiddlewareTBillSellResponse> sellTBill(@RequestBody MiddlewareTBillSellRequest request);

    @GetExchange("/portfolio/activity/recent/{customerId}")
    MiddlewareAppResponse<List<MiddlewarePortfolioActivityResponse>> getRecentPortfolioActivity(@PathVariable String customerId);

    @GetExchange("/portfolio/breakdown/{customerId}")
    MiddlewareAppResponse<List<MiddlewarePortfolioAssetClassBreakdownResponse>> getPortfolioBreakdown(@PathVariable String customerId);

    @PostExchange("/portfolio/statement/request")
    MiddlewareAppResponse<MiddlewarePortfolioStatementResponse> requestPortfolioStatement(@RequestBody MiddlewarePortfolioStatementRequest request);

    @PostExchange("/investments/placements/rate/calculate")
    MiddlewareAppResponse<MiddlewarePlacementRateCalculateResponse> calculatePlacementRate(@RequestBody MiddlewarePlacementPreviewRequest request);
    @PostExchange("/investments/placements/create")
    MiddlewareAppResponse<MiddlewarePlacementCreateFixedDepositResponse> createFixedDeposit(@RequestBody MiddlewarePlacementCreateFixedDepositRequest request);

    @GetExchange("/investments/placements/{customerId}")
    MiddlewareFixedDepositListResponse getFixedDepositsByCustomer(@PathVariable String customerId);

    @GetExchange("/investments/placements/transactions/{fundAccountId}")
    MiddlewareFixedDepositTransactionListResponse getFixedDepositTransactions(@PathVariable String fundAccountId);

    @PostExchange("/investments/placements/liquidate")
    MiddlewareAppResponse<MiddlewarePlacementLiquidateResponse> liquidateFixedDeposit(@RequestBody MiddlewarePlacementLiquidateRequest request);


}

