package org.meristem.oneapp.usersservice.integrations;

import org.meristem.oneapp.usersservice.integrations.requests.DojahBvnVerificationRequest;
import org.meristem.oneapp.usersservice.integrations.responses.DojahBvnLookUpResponse;
import org.meristem.oneapp.usersservice.integrations.responses.DojahBvnVerificationResponse;
import org.meristem.oneapp.usersservice.integrations.responses.DojahNinLookUpResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface DojahClient {

    @GetExchange("/api/v1/kyc/nin") // Test nin 70123456789
    DojahNinLookUpResponse dojahNinLookUp(@RequestParam String nin);

    @GetExchange("/api/v1/kyc/nin/advance") // Test nin 70123456789
    DojahNinLookUpResponse dojahNinLookUpAdvance(@RequestParam String nin);

    @GetExchange("/api/v1/kyc/bvn/full") // Test bvn 22222222222
    DojahBvnLookUpResponse dojahBvnLookUp(@RequestParam String bvn);

    @PostExchange("/api/v1/kyc/bvn/verify")
    DojahBvnVerificationResponse dojahBvnVerify(@RequestBody DojahBvnVerificationRequest request);
}
