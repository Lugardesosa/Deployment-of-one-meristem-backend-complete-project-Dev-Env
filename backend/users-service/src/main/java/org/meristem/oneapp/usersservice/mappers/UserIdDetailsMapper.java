package org.meristem.oneapp.usersservice.mappers;


import org.mapstruct.*;
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
            @Mapping(target = "dob", dateFormat = AppConstants.YYYY_MM_DD, conditionExpression = "java(notBlankOrEmpty(smileIdWebhookNotification.dob()))"),
            @Mapping(target = "dateOfDeath", dateFormat = AppConstants.YYYY_MM_DD, conditionExpression = "java(notBlankOrEmpty(smileIdWebhookNotification.dateOfDeath()))"),
            @Mapping(target = "expirationDate", dateFormat = AppConstants.YYYY_MM_DD, conditionExpression = "java(notBlankOrEmpty(smileIdWebhookNotification.expirationDate()))"),
            @Mapping(target = "issuanceDate", dateFormat = AppConstants.YYYY_MM_DD, conditionExpression = "java(notBlankOrEmpty(smileIdWebhookNotification.issuanceDate()))"),
            @Mapping(target = "gender", ignore = true),
    })
    UserIdDetails smileIdWebhookNotificationToUserIdDetails(SmileIdWebhookNotification smileIdWebhookNotification);

    default boolean notBlankOrEmpty(String value) {
        return StringUtils.hasText(value) && value.matches(AppConstants.DATE_REGEX);
    }
}
