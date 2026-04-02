package org.meristem.oneapp.notificationservice.services;

import org.meristem.oneapp.notificationservice.domains.requests.HollaTagsCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.requests.MiddlewareCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.responses.HollaTagsCallbackResponse;
import org.meristem.oneapp.notificationservice.domains.responses.MiddlewareCallbackResponse;

public interface ICallbackService {
    HollaTagsCallbackResponse handleHollaTags(HollaTagsCallbackRequest request);

    MiddlewareCallbackResponse middlewareCallback(MiddlewareCallbackRequest request);
}
