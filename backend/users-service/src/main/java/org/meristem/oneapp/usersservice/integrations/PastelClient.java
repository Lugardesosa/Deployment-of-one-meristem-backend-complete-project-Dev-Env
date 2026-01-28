package org.meristem.oneapp.usersservice.integrations;

import org.meristem.oneapp.usersservice.integrations.requests.PastelAmlRequest;
import org.meristem.oneapp.usersservice.integrations.responses.PastelAmlResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface PastelClient {

    @PostExchange("/aml/pep/instant")
    PastelAmlResponse searchPepInstant(@RequestBody PastelAmlRequest request);

    @PostExchange("/aml/sanction/instant")
    PastelAmlResponse searchSanctionInstant(@RequestBody PastelAmlRequest request);
}
