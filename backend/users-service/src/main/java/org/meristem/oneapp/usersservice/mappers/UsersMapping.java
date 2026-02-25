package org.meristem.oneapp.usersservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.kafka.dtos.CustomerAddressVerifiedDto;
import org.meristem.oneapp.usersservice.domains.requests.CreateAdminRequest;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.integrations.requests.UpdateAddressRequest;
import org.meristem.oneapp.usersservice.models.Countries;
import org.meristem.oneapp.usersservice.models.CountryStates;
import org.meristem.oneapp.usersservice.models.Users;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsersMapping {

    UsersMapping INSTANCE = Mappers.getMapper(UsersMapping.class);
    Users createUserRequestToUsers(CreateUserRequest user);
    UsersResponse usersToUserResponse(Users user);
    Users createAdminRequestToUsers(CreateAdminRequest adminRequest);

    List<CountriesResponse> countriesToCountriesResponse(List<Countries> content);

    List<StatesResponse> countryStatesToStatesResponseResponse(List<CountryStates> content);

    Users ninQueryResponseToUsers(IdQueryDetailsDto ninQueryResponse);

    UpdateAddressRequest customerAddressVerifiedDtoToUpdateAddressRequest(CustomerAddressVerifiedDto value);
}
