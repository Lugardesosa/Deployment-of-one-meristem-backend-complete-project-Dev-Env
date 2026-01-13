package org.meristem.oneapp.notificationservice.services;

import org.meristem.oneapp.notificationservice.domains.requests.HollaTagsCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.responses.HollaTagsCallbackResponse;

public interface ICallbackService {
    HollaTagsCallbackResponse handleHollaTags(HollaTagsCallbackRequest request);
}
