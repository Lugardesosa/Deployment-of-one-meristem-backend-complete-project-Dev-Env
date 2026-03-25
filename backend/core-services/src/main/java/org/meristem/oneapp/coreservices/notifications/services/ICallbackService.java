package org.meristem.oneapp.coreservices.notifications.services;

import org.meristem.oneapp.coreservices.notifications.domains.requests.HollaTagsCallbackRequest;
import org.meristem.oneapp.coreservices.notifications.domains.requests.TransferPaymentRequest;
import org.meristem.oneapp.coreservices.notifications.domains.responses.HollaTagsCallbackResponse;
import org.meristem.oneapp.coreservices.notifications.domains.responses.MiddlewareTransactionResponse;

public interface ICallbackService {
    HollaTagsCallbackResponse handleHollaTags(HollaTagsCallbackRequest request);

    MiddlewareTransactionResponse transactionCallback(TransferPaymentRequest request);
}
