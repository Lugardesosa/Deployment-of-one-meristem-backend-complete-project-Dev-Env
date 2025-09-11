package org.meristem.oneapp.trustiesservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trustiesservice.controllers.AssetDeleteResponse;
import org.meristem.oneapp.trustiesservice.domains.enums.*;
import org.meristem.oneapp.trustiesservice.domains.enums.Assets;
import org.meristem.oneapp.trustiesservice.domains.requests.*;
import org.meristem.oneapp.trustiesservice.domains.responses.*;
import org.meristem.oneapp.trustiesservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.trustiesservice.mappers.AssetMapper;
import org.meristem.oneapp.trustiesservice.models.*;
import org.meristem.oneapp.trustiesservice.repositories.CustomRepository;
import org.meristem.oneapp.trustiesservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.meristem.oneapp.trustiesservice.dtos.sql.RowMappers.getFileRowMapper;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AssetService {

    private final AssetMapper assetMapper = AssetMapper.INSTANCE;
    private final CustomRepository customRepository;
    private final ActivityLogService activityLogService;

    public CashResponse saveCash(CashRequest request) {
        Cash cash = assetMapper.cashRequestToCash(request);
        cash.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(cash);
        activityLogService.sendActivity(Cash.class, ActivityLogType.CREATED, cash.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Cash", cash.getAccountName()));
        return assetMapper.cashToCashResponse(customRepository.save(cash));
    }

    public PublicEquitiesResponse savePublicEquities(PublicEquitiesRequest cashRequest) {

        PublicEquities publicEquities = assetMapper.publicEquitiesRequestToPublicEquities(cashRequest);
        publicEquities.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(publicEquities);
        activityLogService.sendActivity(PublicEquities.class, ActivityLogType.CREATED, publicEquities.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Public Equities", publicEquities.getShareName()));
        return assetMapper.publicEquitiesToPublicEquitiesResponse(publicEquities);
    }

    public EquitiesResponse savePrivateEquities(EquitiesRequest request) {

        PrivateEquities privateEquitiesRequest = assetMapper.privateEquitiesRequestToPrivateEquities(request);
        privateEquitiesRequest.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(privateEquitiesRequest);
        activityLogService.sendActivity(PrivateEquities.class, ActivityLogType.CREATED, privateEquitiesRequest.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Private Equities", privateEquitiesRequest.getShareName()));
        return assetMapper.privateEquitiesToEquitiesResponse(privateEquitiesRequest);
    }

    public RealEstateResponse saveRealEstate(RealEstateRequest request) {

        RealEstate realEstate = assetMapper.realEstateRequestToRealEstate(request);

        Long userId = AppUtil.getLoggedInUserId();
        realEstate.setOwnerId(userId);
        customRepository.save(realEstate);

        if (nonNull(request.getDocumentRequest())) {
            Files file = Files.builder()
                    .fileKey(request.getDocumentRequest().getFileKey())
                    .contentType(request.getDocumentRequest().getContentType())
                    .fileType(request.getDocumentRequest().getFileType())
                    .ownerId(userId).build();
            customRepository.save(file);
            EntityFiles entityFiles = EntityFiles.builder().fileId(file.getId()).entityId(realEstate.getId())
                    .entityName(RealEstate.class.getSimpleName()).build();
            customRepository.save(entityFiles);
        }
        activityLogService.sendActivity(RealEstate.class, ActivityLogType.CREATED, realEstate.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Real Estate", realEstate.getPropertyAddress()));
        return assetMapper.realEstateToRealEstateResponse(realEstate);
    }

    public MoneyMarketResponse saveMoneyMarket(MoneyMarketRequest request) {
        MoneyMarket moneyMarket = assetMapper.moneyMarketRequestToMoneyMarket(request);
        moneyMarket.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(moneyMarket);
        activityLogService.sendActivity(MoneyMarket.class, ActivityLogType.CREATED, moneyMarket.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Fixed Income/Money Market", moneyMarket.getInvestmentHouse()));
        return assetMapper.moneyMarketToMoneyMarketResponse(moneyMarket);
    }

    public IntellectualPropertyResponse saveIntellectualProperty(IntellectualPropertyRequest request) {
        IntellectualProperty intellectualProperty = assetMapper.intellectualPropertyRequestToIntellectualProperty(request);
        intellectualProperty.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(intellectualProperty);
        activityLogService.sendActivity(IntellectualProperty.class, ActivityLogType.CREATED, intellectualProperty.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Intellectual Property", intellectualProperty.getRegisteredName()));
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
        activityLogService.sendActivity(AlternateAssets.class, ActivityLogType.CREATED, alternateAssets.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Alternate Assets", alternateAssets.getAssetType() + " | " + alternateAssets.getPlatform()));
        return assetMapper.alternateAssetsToAlternateAssetsResponse(alternateAssets);
    }

    public PersonalAssetsResponse savePersonalAssets(PersonalAssetsRequest request) {
        PersonalAssets personalAssets = assetMapper.personalAssetsRequestToPersonalAssets(request);
        personalAssets.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(personalAssets);
        activityLogService.sendActivity(PersonalAssets.class, ActivityLogType.CREATED, personalAssets.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Personal Assets", personalAssets.getAssetType()));
        return assetMapper.personalAssetsToPersonalAssetsResponse(personalAssets);
    }

    public PensionResponse savePension(PensionRequest request) {
        Pension pension = assetMapper.pensionRequestToPension(request);
        pension.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(pension);
        activityLogService.sendActivity(Pension.class, ActivityLogType.CREATED, pension.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Pension", pension.getRsa()));
        return assetMapper.pensionToPensionResponse(pension);
    }

    public LifeInsuranceResponse saveLifeInsurance(LifeInsuranceRequest request) {
        LifeInsurance lifeInsurance = assetMapper.lifeInsuranceRequestToLifeInsurance(request);
        lifeInsurance.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(lifeInsurance);
        activityLogService.sendActivity(LifeInsurance.class, ActivityLogType.CREATED, lifeInsurance.getId(), new HashMap<>(),
                String.format(ActivityLogNote.ASSET_CATEGORY_CREATED.getDescription(), "Life Insurance", lifeInsurance.getInsuranceCompany()));
        return assetMapper.lifeInsuranceToLifeInsuranceResponse(lifeInsurance);
    }


    public SuccessResponse updateEstimatedValue(EstimatedValueRequest request) {

        if (request.value().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Estimated value cannot be zero");
        }

        if (request.assetId() < 1 ) {
            throw new BadRequestException("Asset ID cannot be less than 1");
        }
        Map<String, Object> updates = new HashMap<>();
        updates.put("estimated_amount", request.value());

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", request.assetId());

        int updated = customRepository.dynamicUpdate(request.asset().getClazz(), updates, conditions);
        return SuccessResponse.builder().status(updated > 0).message(updated > 0 ? "Completed" : "Failed").build();
    }

    public GetAssetResponse getAssets(Assets asset, Long assetId) {

        Map<String, Object> filter = new HashMap<>();
        if (assetId != null) {
            filter.put("id", assetId);
        }
        filter.put("owner_id", AppUtil.getLoggedInUserId());
        filter.put("status", EntityStatus.ACTIVE.getValue());
        List<?> all = customRepository.findAll(asset.getClazz(), filter, asset.getRowMapper());
        if (asset.equals(Assets.REAL_ESTATE)) {
            Map<String, Object> fileFilter = new HashMap<>();
            fileFilter.put("owner_id", AppUtil.getLoggedInUserId());
            fileFilter.put("entity_name", RealEstate.class.getSimpleName());
            all.forEach(a -> {
                RealEstateResponse r = (RealEstateResponse) a;
                fileFilter.put("entity_id", r.getId());
                Optional<RealEstateResponse.DocumentResponse> document = customRepository.findFile(Files.class, fileFilter, getFileRowMapper());
                r.setDocumentResponse(document.orElse(null));
            });
        }
        return new GetAssetResponse(all);
    }


    public Map<String, List<?>> getAllAssets() {

        Map<String, List<?>> all = new HashMap<>();

        for (Assets value : Assets.values()) {
            all.put(value.getName(), getAssets(value, null).results());
        }
        return all;
    }

    public GetAssetValueResponse getAssetsValue(Assets asset) {

        List<GetAssetValueResponse.EstimatedValueDetails> estimatedValueDetails;
        Long loggedInUserId = AppUtil.getLoggedInUserId();
        if (nonNull(asset)) {
            estimatedValueDetails = customRepository.getEstimatedValue(asset, loggedInUserId);
        } else {
            estimatedValueDetails = customRepository.getEstimatedValue(loggedInUserId);
        }

        estimatedValueDetails.forEach(e -> {
            e.setAssetEstimatedValueDetails(customRepository.getCurrencyEstimatedValue(e.getCurrencyId(), loggedInUserId));
        });

        return new GetAssetValueResponse(estimatedValueDetails);
    }

    public AssetDeleteResponse deleteAsset(Assets assets, long assetId) {

        Map<String, Object> filter = new HashMap<>();
        filter.put("id", assetId);
        filter.put("owner_id", AppUtil.getLoggedInUserId());
        customRepository.dynamicDelete(PlanAssets.class, Map.of("asset_id", assetId, "asset_type", assets.name()));
        int deleted = customRepository.dynamicDelete(assets.getClazz(), filter);

        activityLogService.sendActivity(assets.getClazz(), ActivityLogType.DELETED, assetId, Map.of("asset_id", assetId, "asset_type", assets.name(), "deleted", deleted + ""),
                String.format(ActivityLogNote.ASSET_REMOVED.getDescription(), assets.getDisplayName()));
        return new AssetDeleteResponse(deleted > 0 ? "Deleted" : "No item deleted", deleted > 0);
    }
}
