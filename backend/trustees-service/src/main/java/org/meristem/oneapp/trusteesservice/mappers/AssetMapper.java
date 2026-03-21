package org.meristem.oneapp.trusteesservice.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.trusteesservice.domains.requests.*;
import org.meristem.oneapp.trusteesservice.domains.responses.*;
import org.meristem.oneapp.trusteesservice.models.*;


@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AssetMapper {

    AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

    @Mapping(source = "currencyId", target = "currencyId")
    Cash cashRequestToCash(CashRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    CashResponse cashToCashResponse(Cash cash);

    @Mapping(source = "currencyId", target = "currencyId")
    PublicEquities publicEquitiesRequestToPublicEquities(PublicEquitiesRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    PublicEquitiesResponse publicEquitiesToPublicEquitiesResponse(PublicEquities publicEquities);

    @Mapping(source = "currencyId", target = "currencyId")
    PrivateEquities privateEquitiesRequestToPrivateEquities(EquitiesRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    EquitiesResponse privateEquitiesToEquitiesResponse(PrivateEquities privateEquities);

    @Mappings(value = {
            @Mapping(source = "currencyId", target = "currencyId"),
    })
    RealEstate realEstateRequestToRealEstate(RealEstateRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    RealEstateResponse realEstateToRealEstateResponse(RealEstate realEstate);

    @Mapping(source = "currencyId", target = "currencyId")
    MoneyMarket moneyMarketRequestToMoneyMarket(MoneyMarketRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    MoneyMarketResponse moneyMarketToMoneyMarketResponse(MoneyMarket moneyMarket);

    @Mapping(source = "currencyId", target = "currencyId")
    IntellectualProperty intellectualPropertyRequestToIntellectualProperty(IntellectualPropertyRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    IntellectualPropertyResponse intellectualPropertyToIntellectualPropertyResponse(IntellectualProperty intellectualProperty);

    @Mappings(value = {
            @Mapping(source = "currencyId", target = "currencyId"),
            @Mapping(ignore = true, target = "assetType")
    })
    AlternateAssets alternateAssetsRequestToAlternateAssets(AlternateAssetsRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    AlternateAssetsResponse alternateAssetsToAlternateAssetsResponse(AlternateAssets alternateAssets);

    @Mapping(source = "currencyId", target = "currencyId")
    PersonalAssets personalAssetsRequestToPersonalAssets(PersonalAssetsRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    PersonalAssetsResponse personalAssetsToPersonalAssetsResponse(PersonalAssets personalAssets);

    @Mapping(source = "currencyId", target = "currencyId")
    Pension pensionRequestToPension(PensionRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    PensionResponse pensionToPensionResponse(Pension pension);

    @Mapping(source = "currencyId", target = "currencyId")
    LifeInsurance lifeInsuranceRequestToLifeInsurance(LifeInsuranceRequest request);

    @Mapping(source = "currencyId", target = "currencyId")
    LifeInsuranceResponse lifeInsuranceToLifeInsuranceResponse(LifeInsurance lifeInsurance);
}
