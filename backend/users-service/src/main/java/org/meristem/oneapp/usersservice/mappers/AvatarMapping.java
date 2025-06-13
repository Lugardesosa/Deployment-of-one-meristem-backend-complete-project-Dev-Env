package org.meristem.oneapp.usersservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.domains.responses.AvatarUrls;
import org.meristem.oneapp.usersservice.models.Images;


@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AvatarMapping {
    AvatarMapping INSTANCE = Mappers.getMapper(AvatarMapping.class);

    AvatarUrls avatarsToAvatarUrls(Images avatars);
}
