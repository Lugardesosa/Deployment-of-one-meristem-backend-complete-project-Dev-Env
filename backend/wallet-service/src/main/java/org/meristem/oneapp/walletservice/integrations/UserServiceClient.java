package org.meristem.oneapp.walletservice.integrations;


import org.meristem.oneapp.walletservice.integrations.requests.VerifyPinRequest;
import org.meristem.oneapp.walletservice.integrations.responses.AppBaseResponse;
import org.meristem.oneapp.walletservice.integrations.responses.UpdateResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface UserServiceClient {

    @PostExchange(url = "/base/verify-pin")
    AppBaseResponse<UpdateResponse> verifyUsersPin(@RequestBody VerifyPinRequest request);
}
