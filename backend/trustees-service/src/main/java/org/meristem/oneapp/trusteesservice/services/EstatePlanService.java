package org.meristem.oneapp.trusteesservice.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trusteesservice.domains.enums.*;
import org.meristem.oneapp.trusteesservice.domains.requests.*;
import org.meristem.oneapp.trusteesservice.domains.responses.EstatePlanResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.GetBeneficiaryPlansResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.GetPlanResponse;
import org.meristem.oneapp.trusteesservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.trusteesservice.integrations.UserServiceClient;
import org.meristem.oneapp.trusteesservice.integrations.responses.BeneficiaryResponse;
import org.meristem.oneapp.trusteesservice.mappers.PlanMapper;
import org.meristem.oneapp.trusteesservice.models.*;
import org.meristem.oneapp.trusteesservice.repositories.CustomRepository;
import org.meristem.oneapp.trusteesservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final ActivityLogService activityLogService;
    private final UserServiceClient userServiceClient;

    public EstatePlanResponse saveSimpleWill(CreateWillRequest request) {

        SimpleWill simpleWill = planMapper.simpleWillRequestToSimpleWill(request);
        simpleWill.setMetainfo(getMetainfo(Plans.SIMPLE_WILL));
        saveWill(simpleWill, request, Plans.SIMPLE_WILL);

        activityLogService.sendActivity(Plans.SIMPLE_WILL.getClazz(), ActivityLogType.DELETED, simpleWill.getId(), Map.of(),
                String.format(ActivityLogNote.WILL_CREATED.getDescription(), "Simple Will"));
        return EstatePlanResponse.builder().status(true).message("Completed").id(simpleWill.getId()).build();
    }

    public EstatePlanResponse saveComprehensiveWill(CreateComprehensiveWillRequest request) {

        ComprehensiveWill comprehensiveWill = planMapper.comprehensiveWillRequestToComprehensiveWill(request);
        YesOrNo yesOrNo = YesOrNo.valueOf(comprehensiveWill.getCustomaryTradition());

        if (yesOrNo == YesOrNo.YES) {
            if (isNull(request.getTraditionDetails())) {
                throw new BadRequestException("TraditionDetails is required if customary tradition is Yes");
            }
            comprehensiveWill.setTraditionDetails(request.getTraditionDetails());
        }

        comprehensiveWill.setMetainfo(getMetainfo(Plans.COMPREHENSIVE_WILL));
        comprehensiveWill.setReligion(Religion.valueOf(request.getReligion()).getValue());
        comprehensiveWill.setMarriageType(MarriageType.valueOf(request.getMarriageType()).getValue());

        saveWill(comprehensiveWill, request, Plans.COMPREHENSIVE_WILL);
        activityLogService.sendActivity(Plans.COMPREHENSIVE_WILL.getClazz(), ActivityLogType.DELETED, comprehensiveWill.getId(), Map.of(),
                String.format(ActivityLogNote.WILL_CREATED.getDescription(), "Comprehensive Will"));
        return EstatePlanResponse.builder().status(true).message("Completed").id(comprehensiveWill.getId()).build();
    }

    public void saveWill (Wills will, CreateWillRequest request, Plans plans) {

        Long userId = getOwnerId(request.getOwnerId());
        will.setOwnerId(userId);
        will.setStatus(EntityStatus.ACTIVE.getValue());
        will.setMaritalStatus(MaritalStatus.valueOf(request.getMaritalStatus()).getValue());
        customRepository.save(will);
        List<PlanAssets> planAssets = request.getAssetIds().assetIds().stream().map(a -> PlanAssets.builder()
                .assetId(a.assetId())
                .assetType(a.assetType())
                .planId(will.getId())
                .planType(plans.name())
                .build()).toList();
        // Maps beneficiary IDs to plan beneficiaries for persistence
        List<PlanBeneficiaries> planBeneficiaries = request.getBeneficiaryIds().stream().map(b ->
                PlanBeneficiaries.builder().beneficiaryId(b).planType(plans.name()).planId(will.getId()).build()).toList();
        customRepository.saveAll(planAssets);
        customRepository.saveAll(planBeneficiaries);

        List<WillExecutors> willExecutors = request.getWillExecutorRequests().stream().map(w ->
                        WillExecutors.builder().ownerId(userId).willExecutorName(w.getWillExecutorName()).planType(plans.name())
                                .planId(will.getId()).willExecutorAddress(w.getWillExecutorAddress()).build())
                .toList();
        customRepository.saveAll(willExecutors);
    }


    public EstatePlanResponse addBeneficiary(AddBeneficiaryRequest request) {

        checkIfUserOwnsPlan(request.ownerId(), request.planType(), request.planId());

        customRepository.saveAll(request.beneficiaryIds().stream().map(b ->
                PlanBeneficiaries.builder().beneficiaryId(b)
                        .planType(request.planType()).planId(request.planId()).build()).toList());

        int beneficiarySize = request.beneficiaryIds().size();
        activityLogService.sendActivity(PlanBeneficiaries.class, ActivityLogType.ADDED, null, Map.of("ids", request.beneficiaryIds()),
                String.format(ActivityLogNote.NEW_BENEFICIARY_ADDED.getDescription(), beneficiarySize, beneficiarySize == 1 ? "Beneficiary" : "Beneficiaries", AppUtil._upperCaseToTitleCase(request.planType())));
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse removeBeneficiary(RemoveBeneficiaryRequest request) {

        checkIfUserOwnsPlan(request.ownerId(), request.planType(), request.planId());

        List<PlanBeneficiaries> beneficiaries = customRepository.findAllBy(PlanBeneficiaries.class, Map.of("planId", request.planId(), "beneficiaryId", request.beneficiaryIds(), "planType", request.planType()));
        customRepository.deleteAll(beneficiaries);

        activityLogService.sendActivity(PlanBeneficiaries.class, ActivityLogType.REMOVED, null, Map.of("ids", request.beneficiaryIds()),
                String.format(ActivityLogNote.BENEFICIARY_REMOVED.getDescription(), beneficiaries.size(), beneficiaries.size() == 1 ? "Beneficiary" : "Beneficiaries", AppUtil._upperCaseToTitleCase(request.planType())));
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse addAsset(AddAssetRequest request) {
        if (isNull(request.planId())) {
            throw new BadRequestException("planId is required");
        }
        List<PlanAssets> planAssets = customRepository.saveAll(request.assetIds().stream().map(a -> PlanAssets.builder()
                .assetId(a.assetId())
                .assetType(a.assetType())
                .planId(request.planId())
                .planType(request.planType())
                .build()).toList());

        activityLogService.sendActivity(PlanAssets.class, ActivityLogType.ADDED, null, Map.of("ids", request.assetIds()),
                String.format(ActivityLogNote.ASSET_ASSIGNED.getDescription(), planAssets.size(), planAssets.size() == 1 ? "Asset" : "Assets", AppUtil._upperCaseToTitleCase(request.planType())));
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse removeAsset(RemoveAssetRequest request) {
        List<PlanAssets> planAssets = customRepository.findAllBy(PlanAssets.class, Map.of("assetType", request.assetType(), "assetId", request.assetIds(), "planType", request.planType(), "planId", request.planId()));
        customRepository.deleteAll(planAssets);

        activityLogService.sendActivity(PlanAssets.class, ActivityLogType.REMOVED, null, Map.of("ids", request.assetIds()),
                String.format(ActivityLogNote.ASSET_REMOVED.getDescription(), planAssets.size(), planAssets.size() == 1 ? "Asset" : "Assets", AppUtil._upperCaseToTitleCase(request.planType())));
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse addExecutor(AddExecutorRequest request) {

        checkIfUserOwnsPlan(request.ownerId(), request.planType(), request.planId());

        List<WillExecutors> willExecutors = customRepository.saveAll(request.executorRequests().stream().map(w ->
                        WillExecutors.builder().ownerId(getOwnerId(request.ownerId())).willExecutorName(w.willExecutorName()).willExecutorAddress(w.willExecutorAddress())
                                .planType(request.planType())
                                .planId(request.planId()).build())
                .toList());

        activityLogService.sendActivity(WillExecutors.class, ActivityLogType.ADDED, null, Map.of("names", request.executorRequests().stream().map(AddExecutorRequest.ExecutorRequest::willExecutorName).toList()),
                String.format(ActivityLogNote.WILL_EXECUTOR_ADDED.getDescription(), willExecutors.size(), willExecutors.size() == 1 ? "Executor" : "Executors", AppUtil._upperCaseToTitleCase(request.planType())));
        return EstatePlanResponse.builder().status(true).message("Completed").build();
    }

    public EstatePlanResponse saveNominatedFund(CreateNominatedFundRequest request) {

        Long userId = getOwnerId(request.ownerId());

        NominatedFund nominatedFund = planMapper.createNominatedFundRequestToNominatedFund(request);
        nominatedFund.setOwnerId(userId);
        nominatedFund.setMetainfo(getMetainfo(Plans.NOMINATED_FUND));
        customRepository.save(nominatedFund);
        List<PlanBeneficiaries> planBeneficiaries = request.beneficiaryInformation().stream().map(b ->
                PlanBeneficiaries.builder().planType(Plans.NOMINATED_FUND.name())
                        .planId(nominatedFund.getId()).beneficiaryId(b.beneficiaryId())
                        .percentage(b.percentage()).build()).toList();
        customRepository.saveAll(planBeneficiaries);

        activityLogService.sendActivity(NominatedFund.class, ActivityLogType.CREATED, nominatedFund.getId(), Map.of(),
                String.format(ActivityLogNote.WILL_CREATED.getDescription(), "Nominated Fund"));
        return EstatePlanResponse.builder().status(true).message("Completed").id(nominatedFund.getId()).build();
    }

    public GetPlanResponse getPlans(Plans plan, Long planId, Long ownerId) {

        ownerId = getOwnerId(ownerId);
        Map<String, Object> filter = new HashMap<>();
        if (planId != null) {
            filter.put("id", planId);
        }
        filter.put("owner_id", ownerId);
        filter.put("plan_type", plan.name());

        return new GetPlanResponse(customRepository.findPlans(plan.getClazz(), filter, plan.getRowMapper(), plan.isWithAssets()));
    }


    public Map<String, List<?>> getAllPlans(Long ownerId) {

        ownerId = getOwnerId(ownerId);
        Map<String, List<?>> all = new HashMap<>();

        for (Plans plan : Plans.values()) {
            all.put(plan.getName(), getPlans(plan, null, ownerId).results());
        }
        return all;
    }

    public EstatePlanResponse savePrivateTrust(@Valid CreatePrivateTrustsRequest request) {

        PrivateTrusts privateTrusts = planMapper.privateTrustsRequestToPrivateTrusts(request);

        Long userId = getOwnerId(request.ownerId());

        privateTrusts.setOwnerId(userId);
        privateTrusts.setMetainfo(getMetainfo(Plans.PRIVATE_TRUSTS));
        customRepository.save(privateTrusts);

        List<PlanBeneficiaries> planBeneficiaries = request.beneficiaryIds().stream().map(b ->
                PlanBeneficiaries.builder().beneficiaryId(b).planType(Plans.PRIVATE_TRUSTS.name()).planId(privateTrusts.getId()).build()).toList();
        customRepository.saveAll(planBeneficiaries);

        List<DesignatedRepresentative> designatedRepresentatives = request.designatedRepresentativeRequests().stream().map(d -> DesignatedRepresentative.builder()
                .planId(privateTrusts.getId()).ownerId(userId).representativeAddress(d.representativeAddress())
                .representativeEmail(d.representativeAddress()).representativeName(d.representativeName())
                .representativePhoneNumber(d.representativePhoneNumber()).build()).toList();
        customRepository.saveAll(designatedRepresentatives);

        activityLogService.sendActivity(PrivateTrusts.class, ActivityLogType.CREATED, privateTrusts.getId(), Map.of(),
                String.format(ActivityLogNote.WILL_CREATED.getDescription(), "Private Trusts"));
        return EstatePlanResponse.builder().status(true).message("Plan created").id(privateTrusts.getId()).build();
    }

    public GetBeneficiaryPlansResponse getBeneficiaryValue(Long beneficiaryId, Long ownerId) {

        Long userId = getOwnerId(ownerId);

        BeneficiaryResponse beneficiary = userServiceClient.getBeneficiary(beneficiaryId, userId).data();
        GetBeneficiaryPlansResponse response = planMapper.beneficiaryResponseToGetBeneficiaryPlansResponse(beneficiary);
        response.setDetails(customRepository.getBeneficiaryPlans(beneficiaryId, userId));
        return response;
    }

    private String getMetainfo(Plans plans) {

        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM, yyyy hh:mm a"));

        return switch (plans) {
            case SIMPLE_WILL -> "Simple Will - " + formattedDate;
            case COMPREHENSIVE_WILL -> "Comprehensive Will - " + formattedDate;
            case NOMINATED_FUND -> "Nominated Fund - " + formattedDate;
            case PRIVATE_TRUSTS -> "Private Trusts - " + formattedDate;
        };
    }

    private void checkIfUserOwnsPlan(Long ownerId, String planType, Long planId) {
        Long userId = getOwnerId(ownerId);


        if (isNull(getPlans(Plans.valueOf(planType), planId, userId))) {
            throw new BadRequestException("Plan does not exist");
        }
    }

    /**
     * Retrieves the owner ID based on the current user's role and provided input.
     * If the current user is an admin, the method returns the provided ownerId.
     * If the current user is not an admin, it returns the ID of the logged-in user.
     *
     * @param ownerId the ID of the owner, required if the current user is an admin
     * @return the determined owner ID based on the user's role and the provided value
     * @throws BadRequestException if the current user is an admin and ownerId is null
     */
    private Long getOwnerId(Long ownerId) {
        if (AppUtil.isAdmin()) {
            if (isNull(ownerId)) {
                throw new BadRequestException("ownerId is required");
            }
            return ownerId;
        } else {
            return AppUtil.getLoggedInUserId();
        }
    }
}
