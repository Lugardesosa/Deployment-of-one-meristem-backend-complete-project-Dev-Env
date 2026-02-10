package org.meristem.oneapp.usersservice.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
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
    UserIdDetails smileIdWebhookNotificationToUserIdDetails(SmileIdWebhookNotification smileIdWebhookNotification);

    default boolean notBlankOrEmpty(String value) {
        return StringUtils.hasText(value) && value.matches(AppConstants.DATE_REGEX);
    }
}
