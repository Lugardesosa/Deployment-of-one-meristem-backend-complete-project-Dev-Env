package org.meristem.oneapp.usersservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.domains.responses.AvatarUrls;
import org.meristem.oneapp.usersservice.models.Files;


@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AvatarMapper {
    AvatarMapper INSTANCE = Mappers.getMapper(AvatarMapper.class);

    AvatarUrls avatarsToAvatarUrls(Files avatars);
}
