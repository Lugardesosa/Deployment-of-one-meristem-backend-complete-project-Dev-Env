package org.meristem.oneapp.usersservice.integrations;

import org.meristem.oneapp.usersservice.integrations.responses.DojahLookUpResponse;
import org.meristem.oneapp.usersservice.integrations.responses.DojahNinLookUpResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface DojahClient {

    @GetMapping("/api/v1/kyc/nin") // Test nin 70123456789
    DojahNinLookUpResponse dojahNinLookUp(@RequestParam String nin);

    @GetMapping("/api/v1/kyc/nin/advance") // Test nin 70123456789
    DojahNinLookUpResponse dojahNinLookUpAdvance(@RequestParam String nin);

    @GetMapping("/api/v1/kyc/bvn/full") // Test nin 22222222222
    DojahLookUpResponse dojahBvnLookUp(@RequestParam String bvn);
}
