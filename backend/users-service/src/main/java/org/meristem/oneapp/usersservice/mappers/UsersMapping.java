package org.meristem.oneapp.usersservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.kafka.dtos.CustomerAddressVerifiedDto;
import org.meristem.oneapp.usersservice.domains.requests.CreateAdminRequest;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserDependentRequest;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.integrations.requests.UpdateAddressRequest;
import org.meristem.oneapp.usersservice.integrations.responses.MiddlewareCustomerResponse;
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

    @Mappings(value = {
            @Mapping(source = "emailAddress", target = "email"),
            @Mapping(source = "phoneNumbers", target = "phoneNumber"),
            @Mapping(source = "otherName", target = "middleName"),
            @Mapping(target = "password", ignore = true)
    })
    Users coreBvnQueryResponseToUser(MiddlewareCustomerResponse.CustomerData bvnQueryResponse);
    Users coreBvnQueryResponseToUser(IdQueryDetailsDto bvnQueryResponse);

    @Mappings(value = {
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "phoneNumber", ignore = true),
            @Mapping(target = "password", ignore = true),
    }
    )
    Users createDependentRequestToUsers(CreateUserDependentRequest userRequest);

    UsersResponse.UsersDetails usersToUsersDetails(Users user);

    @Mappings(value = {
            @Mapping(target = "phoneNumber", source = "phoneNo"),
            @Mapping(target = "email", source = "emailAddress"),
            @Mapping(target = "gender", source = "genderCode"),
            @Mapping(target = "country", source = "customerCountry"),
            @Mapping(target = "residenceState", source = "customerCity"),
            @Mapping(target = "address", source = "customerAddress"),
            @Mapping(target = "dateOfBirth", source = "birthDate"),
            @Mapping(target = "middleName", source = "otherName"),
            @Mapping(target = "emailVerified", ignore = true),
            @Mapping(target = "phoneNumberVerified", ignore = true),
            @Mapping(target = "bvnFacialVerified", ignore = true),
            @Mapping(target = "passwordSet", ignore = true),
    })
    IdQueryDetailsDto middlewareCustomerResponseToIdQueryDetailsDto(MiddlewareCustomerResponse.CustomerData r);
}
