package org.meristem.oneapp.usersservice.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.domains.requests.BeneficiaryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BeneficiaryResponse;
import org.meristem.oneapp.usersservice.models.Beneficiaries;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BeneficiaryMapper {

    BeneficiaryMapper INSTANCE = Mappers.getMapper(BeneficiaryMapper.class);

    @Mappings({
            @Mapping(source = "gender.capitalized", target = "gender"),
            @Mapping(source = "beneficiaryRelationship.value", target = "beneficiaryRelationship"),
            @Mapping(source = "maritalStatus.value", target = "maritalStatus")
    })
    Beneficiaries beneficiaryRequestToBeneficiary(BeneficiaryRequest request);

    BeneficiaryResponse beneficiaryToBeneficiaryResponse(Beneficiaries beneficiaries);

    List<BeneficiaryResponse> beneficiaryToBeneficiaryResponse(List<Beneficiaries> beneficiaries);
}
