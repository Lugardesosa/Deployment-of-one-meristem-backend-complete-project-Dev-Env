package org.meristem.oneapp.notificationservice.services.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jspecify.annotations.Nullable;
import org.meristem.oneapp.kafka.dtos.LoginDto;
import org.meristem.oneapp.kafka.dtos.MessageDetailsDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.dtos.messaging.Message;
import org.meristem.oneapp.notificationservice.mappers.MessageDtoToMessageMapper;

public interface NotificationService<T> {

    void send(T request);

    default Message unbox(MessageDto request, ObjectMapper mapper, @Nullable MessageDtoToMessageMapper messageMapper) {

        if (request.classSimpleName().equals(MessageDetailsDto.class.getSimpleName())) {
            MessageDetailsDto detailsDto = mapper.convertValue(request.message(), MessageDetailsDto.class);
            if (messageMapper != null) {
                return messageMapper.messageDetailsDtoToMessage(detailsDto);
            }
        } else if (request.classSimpleName().equals(LoginDto.class.getSimpleName())) {
            LoginDto loginDto = mapper.convertValue(request.message(), LoginDto.class);
            if (messageMapper != null) {
                Message message = messageMapper.loginDtoToMessage(loginDto);
                message.setBody(buildLoginMail(loginDto));
                return message;
            }
        }
        return null;

    }

    default Message unbox(MessageDto request, ObjectMapper mapper) {
        return unbox(request, mapper, null);
    }

    private String buildLoginMail(LoginDto loginDto) {
        return "Hey people?";
    }
}
