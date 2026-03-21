package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetCountryReferenceResponse(List<GetCRCSCountries> countries) {

    public record GetCRCSCountries(
            @JsonProperty("country_code") String countryCode,
            @JsonProperty("country_description") String countryDescription,
            @JsonProperty("home_yesno") String homeYesno
    ) {}
}
