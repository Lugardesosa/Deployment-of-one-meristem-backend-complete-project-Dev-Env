package org.meristem.oneapp.coreservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservice.domains.enums.AlternateAssetType;
import org.meristem.oneapp.coreservice.domains.enums.Assets;
import org.meristem.oneapp.coreservice.domains.requests.*;
import org.meristem.oneapp.coreservice.domains.responses.*;
import org.meristem.oneapp.coreservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.coreservice.mappers.AssetMapper;
import org.meristem.oneapp.coreservice.models.*;
import org.meristem.oneapp.coreservice.repositories.CustomRepository;
import org.meristem.oneapp.coreservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {

    private final AssetMapper assetMapper = AssetMapper.INSTANCE;
    private final CustomRepository customRepository;

    public CashResponse saveCash(CashRequest request) {
        Cash cash = assetMapper.cashRequestToCash(request);
        cash.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(cash);
        return assetMapper.cashToCashResponse(customRepository.save(cash));
    }

    public PublicEquitiesResponse savePublicEquities(PublicEquitiesRequest cashRequest) {

        PublicEquities publicEquities = assetMapper.publicEquitiesRequestToPublicEquities(cashRequest);
        publicEquities.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(publicEquities);
        return assetMapper.publicEquitiesToPublicEquitiesResponse(publicEquities);
    }

    public EquitiesResponse savePrivateEquities(EquitiesRequest request) {

        PrivateEquities privateEquitiesRequest = assetMapper.privateEquitiesRequestToPrivateEquities(request);
        privateEquitiesRequest.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(privateEquitiesRequest);
        return assetMapper.privateEquitiesToEquitiesResponse(privateEquitiesRequest);
    }

    public RealEstateResponse saveRealEstate(RealEstateRequest request) {

        RealEstate realEstate = assetMapper.realEstateRequestToRealEstate(request);
        realEstate.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(realEstate);
        return assetMapper.realEstateToRealEstateResponse(realEstate);
    }

    public MoneyMarketResponse saveMoneyMarket(MoneyMarketRequest request) {
        MoneyMarket moneyMarket = assetMapper.moneyMarketRequestToMoneyMarket(request);
        moneyMarket.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(moneyMarket);
        return assetMapper.moneyMarketToMoneyMarketResponse(moneyMarket);
    }

    public IntellectualPropertyResponse saveIntellectualProperty(IntellectualPropertyRequest request) {
        IntellectualProperty intellectualProperty = assetMapper.intellectualPropertyRequestToIntellectualProperty(request);
        intellectualProperty.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(intellectualProperty);
        return assetMapper.intellectualPropertyToIntellectualPropertyResponse(intellectualProperty);
    }

    public AlternateAssetsResponse saveAlternateAssets(AlternateAssetsRequest request) {
        AlternateAssets alternateAssets = assetMapper.alternateAssetsRequestToAlternateAssets(request);
        AlternateAssetType alternateAssetType = AlternateAssetType.valueOf(request.getAssetType());

        if (alternateAssetType.name().equals(AlternateAssetType.CRYPTO_NFT.getDescription()) &&
        isNull(alternateAssets.getWalletAddress())) {
            throw new BadRequestException("Wallet address is required");
        }
        alternateAssets.setAssetType(alternateAssetType.getDescription());
        alternateAssets.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(alternateAssets);
        return assetMapper.alternateAssetsToAlternateAssetsResponse(alternateAssets);
    }

    public PersonalAssetsResponse savePersonalAssets(PersonalAssetsRequest request) {
        PersonalAssets personalAssets = assetMapper.personalAssetsRequestToPersonalAssets(request);
        personalAssets.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(personalAssets);
        return assetMapper.personalAssetsToPersonalAssetsResponse(personalAssets);
    }

    public PensionResponse savePension(PensionRequest request) {
        Pension pension = assetMapper.pensionRequestToPension(request);
        pension.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(pension);
        return assetMapper.pensionToPensionResponse(pension);
    }

    public LifeInsuranceResponse saveLifeInsurance(LifeInsuranceRequest request) {
        LifeInsurance lifeInsurance = assetMapper.lifeInsuranceRequestToLifeInsurance(request);
        lifeInsurance.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(lifeInsurance);
        return assetMapper.lifeInsuranceToLifeInsuranceResponse(lifeInsurance);
    }

    public GetAssetResponse getAssets(Assets asset, Long assetId) {

        Map<String, Object> filter = new HashMap<>();
        if (assetId != null) {
            filter.put("id", assetId);
        }
        filter.put("owner_id", AppUtil.getLoggedInUserId());
        return new GetAssetResponse(customRepository.findAll(asset.getClazz(), filter, asset.getRowMapper()));
    }
}
