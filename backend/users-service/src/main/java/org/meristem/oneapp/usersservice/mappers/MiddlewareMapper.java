package org.meristem.oneapp.usersservice.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.kafka.dtos.CreateJointCustomerDto;
import org.meristem.oneapp.usersservice.integrations.requests.CreateJointCustomerRequest;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MiddlewareMapper {

    MiddlewareMapper INSTANCE = Mappers.getMapper(MiddlewareMapper.class);

    CreateJointCustomerRequest createJointCustomerDtoToCreateJointCustomerRequest(CreateJointCustomerDto createJointCustomerDto);
}
