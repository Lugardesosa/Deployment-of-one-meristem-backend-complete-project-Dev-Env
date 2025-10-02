package org.meristem.oneapp.notificationservice.services.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.meristem.oneapp.kafka.dtos.*;
import org.meristem.oneapp.notificationservice.domains.enums.EmailTemplate;
import org.meristem.oneapp.notificationservice.dtos.messaging.Message;
import org.meristem.oneapp.notificationservice.mappers.MessageDtoToMessageMapper;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;


public interface NotificationService<T> {

    void send(T request);

    default Message unbox(MessageDto request, ObjectMapper mapper, MessageDtoToMessageMapper messageMapper) {

        if (request.classSimpleName().equals(OtpDto.class.getSimpleName())) {
            OtpDto detailsDto = mapper.convertValue(request.message(), OtpDto.class);
            Message message = messageMapper.otpDtoToMessage(detailsDto);
            message.setEmailTemplate(EmailTemplate.CONFIRM_VERIFICATION_CODE);
            Map<String, Object> context = new HashMap<>();
            context.put("code", detailsDto.getCode());
            context.put("firstName", nonNull(detailsDto.getFirstName()) ? detailsDto.getFirstName() : "");
            message.setContext(context);
            return message;
        } else if (request.classSimpleName().equals(LoginDto.class.getSimpleName())) {
            LoginDto loginDto = mapper.convertValue(request.message(), LoginDto.class);
            Message message = messageMapper.loginDtoToMessage(loginDto);
            message.setContext(buildLoginMail(loginDto));
            message.setEmailTemplate(EmailTemplate.LOGIN_NOTIFICATION);
            return message;
        } else if (request.classSimpleName().equals(AdminAccountDto.class.getSimpleName())) {
            AdminAccountDto adminAccountDto = mapper.convertValue(request.message(), AdminAccountDto.class);
            Message message = messageMapper.adminAccountDtoToMessage(adminAccountDto);
            message.setEmailTemplate(EmailTemplate.LOGIN_NOTIFICATION);
            return message;
        } else if (request.classSimpleName().equals(PasswordChangeDto.class.getSimpleName())) {
            PasswordChangeDto passwordChangeDto = mapper.convertValue(request.message(), PasswordChangeDto.class);
            Message message = messageMapper.passwordChangeDtoToMessage(passwordChangeDto);
            message.setEmailTemplate(EmailTemplate.LOGIN_NOTIFICATION);
            return message;
        }
        return null;
    }


    private Map<String, Object> buildLoginMail(LoginDto loginDto) {

        String location = StringUtils.hasText(loginDto.getLocation().cityName()) ? loginDto.getLocation().cityName() : "";
        location = StringUtils.hasText(location) && StringUtils.hasText(loginDto.getLocation().country()) ? location.concat(", ") : "";
        location = StringUtils.hasText(loginDto.getLocation().country()) ? location.concat(loginDto.getLocation().country()) : "";
        Map<String, Object> context = new HashMap<>();
        context.put("firstName", loginDto.getFirstname());
        context.put("deviceName", loginDto.getDeviceDetails());
        context.put("date", loginDto.getDate());
        context.put("time", loginDto.getTime());
        context.put("toEmail", Arrays.stream(loginDto.getRecipients()).findFirst().orElse(""));

        context.put("location", location);
        return context;
    }
}
