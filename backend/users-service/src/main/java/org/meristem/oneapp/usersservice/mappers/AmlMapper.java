package org.meristem.oneapp.usersservice.mappers;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.domains.requests.PastelAmlWebhookRequest;
import org.meristem.oneapp.usersservice.dtos.AdverseMediaDto;
import org.meristem.oneapp.usersservice.dtos.PepDto;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AmlMapper {

    AmlMapper INSTANCE = Mappers.getMapper(AmlMapper.class);


    @Mappings(value = {
            @Mapping(target = "confidenceScore", source = "keyword.confidenceScore"),
    })
    AdverseMediaDto adverseResultToAdverseDto(PastelAmlWebhookRequest.AdverseMediaDate.AdverseResult result);


    @Mappings(value = {
            @Mapping(target = "confidenceScore", ignore = true),
            @Mapping(target = "checkCreationDate", ignore = true),
            @Mapping(target = "lastScreenedDate", ignore = true),
            @Mapping(target = "birthDate", qualifiedByName = "stringToListSemiColon"),
    })
    PepDto pepResultToPepDto(PastelAmlWebhookRequest.EntityDataPep.PepData peps);

    List<PepDto> pepResultToPepDtoList(List<PastelAmlWebhookRequest.EntityDataPep.PepData> peps);

    @Named("listToString")
    static String listToString(List<String> values) {
        return String.join(",", values);
    }

    @Named("stringToListSemiColon")
    static List<String> listToString(String values) {
        return List.of(values.split(";"));
    }
}
