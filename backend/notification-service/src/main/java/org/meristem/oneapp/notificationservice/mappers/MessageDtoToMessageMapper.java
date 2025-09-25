package org.meristem.oneapp.notificationservice.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.kafka.dtos.LoginDto;
import org.meristem.oneapp.kafka.dtos.MessageDetailsDto;
import org.meristem.oneapp.notificationservice.dtos.messaging.Message;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageDtoToMessageMapper {

    MessageDtoToMessageMapper INSTANCE = Mappers.getMapper(MessageDtoToMessageMapper.class);

    Message messageDetailsDtoToMessage(MessageDetailsDto dto);

    @Mappings(value = {@Mapping(target = "body", ignore = true),
            @Mapping(target = "recipient", source = "recipients")})
    Message loginDtoToMessage(LoginDto dto);


}
