package org.meristem.oneapp.trusteesservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.trusteesservice.domains.enums.Plans;
import org.meristem.oneapp.trusteesservice.domains.requests.*;
import org.meristem.oneapp.trusteesservice.domains.responses.EstatePlanResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.GetBeneficiaryPlansResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.GetPlanResponse;

import java.util.List;
import java.util.Map;

public interface IEstatePlanService {

    EstatePlanResponse saveSimpleWill(CreateWillRequest request);

    EstatePlanResponse saveComprehensiveWill(CreateComprehensiveWillRequest request);

    EstatePlanResponse addBeneficiary(AddBeneficiaryRequest request);

    EstatePlanResponse removeBeneficiary(RemoveBeneficiaryRequest request);

    EstatePlanResponse addAsset(AddAssetRequest request);

    EstatePlanResponse removeAsset(RemoveAssetRequest request);

    EstatePlanResponse addExecutor(AddExecutorRequest request);

    EstatePlanResponse saveNominatedFund(CreateNominatedFundRequest request);

    GetPlanResponse getPlans(Plans plan, Long planId, Long ownerId);

    Map<String, List<?>> getAllPlans(Long ownerId);

    EstatePlanResponse savePrivateTrust(@Valid CreatePrivateTrustsRequest request);

    GetBeneficiaryPlansResponse getBeneficiaryValue(Long beneficiaryId, Long ownerId);
}
