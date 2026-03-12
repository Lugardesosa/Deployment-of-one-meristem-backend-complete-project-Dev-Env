package org.meristem.oneapp.usersservice.mappers;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.integrations.responses.DojahBvnLookUpResponse;
import org.meristem.oneapp.usersservice.integrations.responses.DojahNinLookUpResponse;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.springframework.util.StringUtils;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserIdDetailsMapper {

    UserIdDetailsMapper INSTANCE = Mappers.getMapper(UserIdDetailsMapper.class);

    @Mappings(value = {
            @Mapping(target = "idType", ignore = true),
            @Mapping(target = "fileId", ignore = true),
            @Mapping(target = "userId", ignore = true),
            @Mapping(target = "dateOfBirth", dateFormat = AppConstants.YYYY_MM_DD, conditionExpression = "java(notBlankOrEmpty(smileIdWebhookNotification.getDateOfBirth()))"),
            @Mapping(target = "gender", ignore = true),
    })
    UserIdDetails smileIdWebhookNotificationToUserIdDetails(IdQueryDetailsDto smileIdWebhookNotification);


    @Mappings(value = {
            @Mapping(source = "image", target = "photo"),
            @Mapping(source = "residentialAddress", target = "address"),
            @Mapping(source = "lgaOfOrigin", target = "localAreaOfOrigin"),
            @Mapping(source = "phoneNumber1", target = "phoneNumber"),
    })
    IdQueryDetailsDto dojahBvnLookupResponseToIdQueryDetailsDto(DojahBvnLookUpResponse.Entity response);

    @Mappings(value = {
            @Mapping(target = "address", expression = "java(addressMapper(response))"),
            @Mapping(target = "localAreaOfOrigin", expression = "java(lgaMapper(response))"),
            @Mapping(target = "residenceState", expression = "java(stateMapper(response))"),
    })
    IdQueryDetailsDto dojahNinLookupResponseToIdQueryDetailsDto(DojahNinLookUpResponse.Entity response);

    IdQueryDetailsDto smileIdBvnLookupResponseToIdQueryDetailsDto(SmileIdWebhookNotification response);

    default boolean notBlankOrEmpty(String value) {
        return StringUtils.hasText(value) && value.matches(AppConstants.DATE_REGEX);
    }

    @Named("addressMapper")
    default String addressMapper(DojahNinLookUpResponse.Entity response) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(response.residenceAddress()) ? response.residenceAddress() : response.originPlace() ;
    }
    @Named("lgaMapper")
    default String lgaMapper(DojahNinLookUpResponse.Entity response) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(response.residenceLga()) ? response.residenceLga() : response.originLga() ;
    }
    @Named("stateMapper")
    default String stateMapper(DojahNinLookUpResponse.Entity response) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(response.residenceState()) ? response.residenceState() : response.originState() ;
    }
}
