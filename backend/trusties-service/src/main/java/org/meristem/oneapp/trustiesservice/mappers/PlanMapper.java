package org.meristem.oneapp.trustiesservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.trustiesservice.domains.requests.*;
import org.meristem.oneapp.trustiesservice.models.*;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PlanMapper {

    PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);

    @Mappings(value = {
        @Mapping(target = "maritalStatus", ignore = true),
        @Mapping(target = "ownerId", ignore = true)
    })
    SimpleWill simpleWillRequestToSimpleWill(CreateWillRequest simpleCreateWillRequest);

    @Mappings(value = {
            @Mapping(target = "marriageType", ignore = true),
            @Mapping(target = "religion", ignore = true),
            @Mapping(target = "traditionDetails", ignore = true),
            @Mapping(target = "maritalStatus", ignore = true),
            @Mapping(target = "ownerId", ignore = true)
    })
    ComprehensiveWill comprehensiveWillRequestToComprehensiveWill(CreateComprehensiveWillRequest comprehensiveRequest);

    WillExecutors willExecutorRequestToWillExecutors(AddExecutorRequest.ExecutorRequest executorRequest);

    @Mapping(target = "ownerId", ignore = true)
    NominatedFund createNominatedFundRequestToNominatedFund(CreateNominatedFundRequest request);

    @Mappings(value = {
            @Mapping(target = "ownerId", ignore = true),
            @Mapping(target = "frequency", source = "frequency.value"),
            @Mapping(target = "objective", source = "objective.value"),
            @Mapping(target = "powerOfTrustee", expression = "java(String.join(\",\", request.powerOfTrustee()))")

    })
    PrivateTrusts privateTrustsRequestToPrivateTrusts(CreatePrivateTrustsRequest request);
}
