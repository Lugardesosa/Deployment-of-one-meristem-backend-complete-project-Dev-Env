package org.meristem.oneapp.usersservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.domains.requests.CreateAdminRequest;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.models.Users;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsersMapping {

    UsersMapping INSTANCE = Mappers.getMapper(UsersMapping.class);
    Users createUserRequestToUsers(CreateUserRequest user);
    UsersResponse usersToUserResponse(Users user);
    Users createAdminRequestToUsers(CreateAdminRequest adminRequest);
}
