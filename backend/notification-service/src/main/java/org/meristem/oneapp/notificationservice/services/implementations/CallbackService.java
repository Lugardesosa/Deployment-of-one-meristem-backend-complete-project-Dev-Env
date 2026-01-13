package org.meristem.oneapp.notificationservice.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.notificationservice.domains.requests.HollaTagsCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.responses.HollaTagsCallbackResponse;
import org.meristem.oneapp.notificationservice.services.ICallbackService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackService implements ICallbackService {
    public HollaTagsCallbackResponse handleHollaTags(HollaTagsCallbackRequest request) {

        return new HollaTagsCallbackResponse("ok");
    }
}
