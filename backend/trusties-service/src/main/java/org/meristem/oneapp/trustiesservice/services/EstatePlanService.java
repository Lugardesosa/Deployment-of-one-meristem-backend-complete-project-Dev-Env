package org.meristem.oneapp.trustiesservice.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.meristem.oneapp.trustiesservice.domains.enums.*;
import org.meristem.oneapp.trustiesservice.domains.requests.*;
import org.meristem.oneapp.trustiesservice.domains.responses.EstatePlanResponse;
import org.meristem.oneapp.trustiesservice.domains.responses.GetPlanResponse;
import org.meristem.oneapp.trustiesservice.mappers.PlanMapper;
import org.meristem.oneapp.trustiesservice.models.*;
import org.meristem.oneapp.trustiesservice.repositories.CustomRepository;
import org.meristem.oneapp.trustiesservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EstatePlanService {

    private final CustomRepository customRepository;
    private final PlanMapper planMapper = PlanMapper.INSTANCE;

    public EstatePlanResponse saveSimpleWill(CreateWillRequest request) {

        SimpleWill simpleWill = planMapper.simpleWillRequestToSimpleWill(request);
        saveWill(simpleWill, request, FormName.SIMPLE_WILL);

        return EstatePlanResponse.builder().status(true).message("Completed").id(simpleWill.getId()).build();
    }

    public EstatePlanResponse saveComprehensiveWill(CreateComprehensiveWillRequest request) {

        ComprehensiveWill comprehensiveWill = planMapper.comprehensiveWillRequestToComprehensiveWill(request);
        YesOrNo yesOrNo = YesOrNo.valueOf(comprehensiveWill.getCustomaryTradition());

        if (yesOrNo == YesOrNo.YES) {
            if (isNull(request.getTraditionDetails())) {
                throw new InvalidRequestException("TraditionDetails is required if customary tradition is Yes");
            }
            comprehensiveWill.setTraditionDetails(request.getTraditionDetails());
        }

        comprehensiveWill.setReligion(Religion.valueOf(request.getReligion()).getValue());
        comprehensiveWill.setMarriageType(MarriageType.valueOf(request.getMarriageType()).getValue());

        saveWill(comprehensiveWill, request, FormName.COMPREHENSIVE_WILL);
        return EstatePlanResponse.builder().status(true).message("Completed").id(comprehensiveWill.getId()).build();
    }

    public void saveWill (Wills will, CreateWillRequest request, FormName assets) {
        Long userId = AppUtil.getLoggedInUserId();
        will.setOwnerId(userId);
        will.setStatus(EntityStatus.ACTIVE.getValue());
        will.setMaritalStatus(MaritalStatus.valueOf(request.getMaritalStatus()).getValue());
        customRepository.save(will);
        List<PlanAssets> planAssets = request.getAssetIds().assetIds().stream().map(a -> PlanAssets.builder()
                .assetId(a.assetId())
                .assetType(a.assetType())
                .planId(will.getId())
                .planType(assets.name())
                .build()).toList();
        List<PlanBeneficiaries> planBeneficiaries = request.getBeneficiaryIds().stream().map(b ->
                PlanBeneficiaries.builder().beneficiaryId(b).planType(assets.name()).planId(will.getId()).build()).toList();
        customRepository.saveAll(planAssets);
        customRepository.saveAll(planBeneficiaries);

        List<WillExecutors> willExecutors = request.getWillExecutorRequests().stream().map(w ->
                        WillExecutors.builder().ownerId(userId).willExecutorName(w.getWillExecutorName())
                                .planId(will.getId()).willExecutorAddress(w.getWillExecutorAddress()).build())
                .toList();
        customRepository.saveAll(willExecutors);

    }


    public EstatePlanResponse addBeneficiary(AddBeneficiaryRequest request) {

        customRepository.saveAll(request.beneficiaryIds().stream().map(b ->
                PlanBeneficiaries.builder().beneficiaryId(b)
                        .planType(request.planType()).planId(request.planId()).build()).toList());
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse removeBeneficiary(RemoveBeneficiaryRequest request) {

        customRepository.deleteAll(customRepository.findAllBy(PlanBeneficiaries.class, Map.of("planId", request.planId(), "beneficiaryId", request.beneficiaryIds(), "planType", request.planType())));

        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse addAsset(AddAssetRequest request) {
        if (isNull(request.planId())) {
            throw new InvalidRequestException("planId is required");
        }
        customRepository.saveAll(request.assetIds().stream().map(a -> PlanAssets.builder()
                .assetId(a.assetId())
                .assetType(a.assetType())
                .planId(request.planId())
                .planType(request.planType())
                .build()).toList());
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse removeAsset(RemoveAssetRequest request) {
        List<PlanAssets> planAssets = customRepository.findAllBy(PlanAssets.class, Map.of("assetType", request.assetType(), "assetId", request.assetIds(), "planType", request.planType(), "planId", request.planId()));
        customRepository.deleteAll(planAssets);
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse addExecutor(AddExecutorRequest request) {

        Long loggedInUserId = AppUtil.getLoggedInUserId();

        customRepository.saveAll(request.executorRequests().stream().map(w ->
                        WillExecutors.builder().ownerId(loggedInUserId).willExecutorName(w.willExecutorName()).willExecutorAddress(w.willExecutorAddress()).build())
                .toList());
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }


    public EstatePlanResponse addWillExecutor(AddExecutorRequest.ExecutorRequest request) {
        WillExecutors willExecutors = planMapper.willExecutorRequestToWillExecutors(request);
        willExecutors.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(willExecutors);
        return EstatePlanResponse.builder().status(true).message("Will created").id(willExecutors.getId()).build();
    }

    public EstatePlanResponse saveNominatedFund(CreateNominatedFundRequest request) {

        NominatedFund nominatedFund = planMapper.createNominatedFundRequestToNominatedFund(request);
        nominatedFund.setOwnerId(AppUtil.getLoggedInUserId());
        customRepository.save(nominatedFund);
        List<PlanBeneficiaries> planBeneficiaries = request.beneficiaryInformation().stream().map(b ->
                PlanBeneficiaries.builder().planType(FormName.NOMINATED_FUND.name())
                        .planId(nominatedFund.getId()).beneficiaryId(b.beneficiaryId())
                        .percentage(b.percentage()).build()).toList();
        customRepository.saveAll(planBeneficiaries);

        return EstatePlanResponse.builder().status(true).message("Completed").id(nominatedFund.getId()).build();
    }

    public GetPlanResponse getPlans(Plans plan, Long planId) {


        Map<String, Object> filter = new HashMap<>();
        if (planId != null) {
            filter.put("id", planId);
        }
        filter.put("owner_id", AppUtil.getLoggedInUserId());

        return new GetPlanResponse(customRepository.findPlans(plan.getClazz(), filter, plan.getRowMapper(), plan.isWithAssets()));
    }

    public EstatePlanResponse savePrivateTrust(@Valid CreatePrivateTrustsRequest request) {

        PrivateTrusts privateTrusts = planMapper.privateTrustsRequestToPrivateTrusts(request);
        Long loggedInUserId = AppUtil.getLoggedInUserId();
        privateTrusts.setOwnerId(loggedInUserId);
        customRepository.save(privateTrusts);

        List<PlanBeneficiaries> planBeneficiaries = request.beneficiaryIds().stream().map(b ->
                PlanBeneficiaries.builder().beneficiaryId(b).planType(PlansEnum.PRIVATE_TRUSTS.name()).planId(privateTrusts.getId()).build()).toList();
        customRepository.saveAll(planBeneficiaries);

        List<DesignatedRepresentative> designatedRepresentatives = request.designatedRepresentativeRequests().stream().map(d -> DesignatedRepresentative.builder()
                .planId(privateTrusts.getId()).ownerId(loggedInUserId).representativeAddress(d.representativeAddress())
                .representativeEmail(d.representativeAddress()).representativeName(d.representativeName())
                .representativePhoneNumber(d.representativePhoneNumber()).build()).toList();
        customRepository.saveAll(designatedRepresentatives);

        return EstatePlanResponse.builder().status(true).message("Plan created").id(privateTrusts.getId()).build();

    }
}
