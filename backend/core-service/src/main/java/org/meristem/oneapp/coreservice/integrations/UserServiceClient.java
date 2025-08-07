package org.meristem.oneapp.coreservice.integrations;


import org.meristem.oneapp.coreservice.integrations.responses.AppBaseResponse;
import org.meristem.oneapp.coreservice.integrations.responses.BeneficiaryResponse;
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
    BeneficiaryResponse getBeneficiary(@RequestParam(name = "email")  String email, @RequestParam(name = "userId") Long userId);
}
