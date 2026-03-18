package org.meristem.oneapp.notificationservice.services;

import org.meristem.oneapp.notificationservice.domains.requests.HollaTagsCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.requests.TransferPaymentRequest;
import org.meristem.oneapp.notificationservice.domains.responses.HollaTagsCallbackResponse;
import org.meristem.oneapp.notificationservice.domains.responses.MiddlewareTransactionResponse;

public interface ICallbackService {
    HollaTagsCallbackResponse handleHollaTags(HollaTagsCallbackRequest request);

    MiddlewareTransactionResponse transactionCallback(TransferPaymentRequest request);
}
