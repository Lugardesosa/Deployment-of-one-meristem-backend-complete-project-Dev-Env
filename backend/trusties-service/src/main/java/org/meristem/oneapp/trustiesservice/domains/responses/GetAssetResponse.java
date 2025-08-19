package org.meristem.oneapp.trustiesservice.domains.responses;



import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response objects for assets")
public record GetAssetResponse(@Schema(oneOf = {CashResponse.class, PublicEquitiesResponse.class, EquitiesResponse.class, RealEstateResponse.class, MoneyMarketResponse.class,
        IntellectualPropertyResponse.class, AlternateAssetsResponse.class, PersonalAssetsResponse.class, PensionResponse.class,
        LifeInsuranceResponse.class}, description = "returns a list of one of the following values") List<?> results) {
}
