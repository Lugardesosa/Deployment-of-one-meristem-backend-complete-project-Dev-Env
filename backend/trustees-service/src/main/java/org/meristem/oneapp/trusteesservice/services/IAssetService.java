package org.meristem.oneapp.trusteesservice.services;

import org.meristem.oneapp.trusteesservice.domains.enums.Assets;
import org.meristem.oneapp.trusteesservice.domains.requests.*;
import org.meristem.oneapp.trusteesservice.domains.responses.*;

import java.util.List;
import java.util.Map;

public interface IAssetService {

    CashResponse saveCash(CashRequest request);

    PublicEquitiesResponse savePublicEquities(PublicEquitiesRequest cashRequest);

    EquitiesResponse savePrivateEquities(EquitiesRequest request);

    RealEstateResponse saveRealEstate(RealEstateRequest request);

    MoneyMarketResponse saveMoneyMarket(MoneyMarketRequest request);

    IntellectualPropertyResponse saveIntellectualProperty(IntellectualPropertyRequest request);

    AlternateAssetsResponse saveAlternateAssets(AlternateAssetsRequest request);

    PersonalAssetsResponse savePersonalAssets(PersonalAssetsRequest request);

    PensionResponse savePension(PensionRequest request);

    LifeInsuranceResponse saveLifeInsurance(LifeInsuranceRequest request);

    SuccessResponse updateEstimatedValue(EstimatedValueRequest request);

    GetAssetResponse getAssets(Assets asset, Long assetId);

    Map<String, List<?>> getAllAssets();

    GetAssetValueResponse getAssetsValue(Assets asset);

    AssetDeleteResponse deleteAsset(Assets assets, long assetId);
}
