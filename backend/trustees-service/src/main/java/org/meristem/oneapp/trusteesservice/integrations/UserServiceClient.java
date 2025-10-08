package org.meristem.oneapp.trusteesservice.integrations;


import org.meristem.oneapp.trusteesservice.integrations.responses.AppBaseResponse;
import org.meristem.oneapp.trusteesservice.integrations.responses.BeneficiaryResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface UserServiceClient {

    @GetExchange(url = "/beneficiary/all")
    AppBaseResponse<List<BeneficiaryResponse>> getAllBeneficiaries(@RequestParam(name = "userId") Long userId);

    @GetExchange(url = "/beneficiary")
    AppBaseResponse<BeneficiaryResponse> getBeneficiary(@RequestParam(name = "id") Long beneficiaryId, @RequestParam(name = "userId") Long userId);
}
