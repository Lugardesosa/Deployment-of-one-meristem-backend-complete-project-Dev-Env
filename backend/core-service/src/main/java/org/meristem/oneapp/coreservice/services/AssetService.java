package org.meristem.oneapp.coreservice.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservice.domains.requests.*;
import org.meristem.oneapp.coreservice.domains.responses.*;
import org.meristem.oneapp.coreservice.mappers.AssetMapper;
import org.meristem.oneapp.coreservice.models.*;
import org.meristem.oneapp.coreservice.repositories.CustomRepository;
import org.meristem.oneapp.coreservice.utils.AppUtil;
import org.springframework.stereotype.Service;

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

    public FintechWalletResponse saveFintechWallet(FintechWalletRequest request) {

        FintechWallets fintechWallets = assetMapper.fintechWalletRequestToFintechWallets(request);
        fintechWallets.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(fintechWallets);
        return assetMapper.fintechWalletToFintechWalletResponse(fintechWallets);
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
        alternateAssets.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(alternateAssets);
        return assetMapper.alternateAssetsToAlternateAssetsResponse(alternateAssets);
    }

    public PersonalAssetsResponse savePersonalAssets(@Valid PersonalAssetsRequest request) {
        PersonalAssets personalAssets = assetMapper.personalAssetsRequestToPersonalAssets(request);
        personalAssets.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(personalAssets);
        return assetMapper.personalAssetsToPersonalAssetsResponse(personalAssets);
    }

    public PensionResponse savePension(@Valid PensionRequest request) {
        Pension pension = assetMapper.pensionRequestToPension(request);
        pension.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(pension);
        return assetMapper.pensionToPensionResponse(pension);
    }

    public LifeInsuranceResponse saveLifeInsurance(@Valid LifeInsuranceRequest request) {
        LifeInsurance lifeInsurance = assetMapper.lifeInsuranceRequestToLifeInsurance(request);
        lifeInsurance.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(lifeInsurance);
        return assetMapper.lifeInsuranceToLifeInsuranceResponse(lifeInsurance);
    }
}
