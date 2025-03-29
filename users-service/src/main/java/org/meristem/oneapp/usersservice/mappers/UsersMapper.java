package org.meristem.oneapp.usersservice.mappers;

import org.mapstruct.Mapper;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.models.Users;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    Users createUserRequestToUsers(CreateUserRequest user);
}
