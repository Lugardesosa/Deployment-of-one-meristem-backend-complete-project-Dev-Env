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

    /**
     * Sends a notification request.
     *
     * @param request the notification payload; its concrete type depends on the implementation of this service
     */
    void send(T request);

    /**
     * Translates a generic MessageDto into the internal Message representation.
     * The conversion uses the provided ObjectMapper and mapping component to handle supported DTO types,
     * enriching the Message with the appropriate email template and context when applicable.
     *
     * @param request        the generic wrapper that carries the DTO type information and its payload
     * @param mapper         the ObjectMapper used to convert the payload into a concrete DTO
     * @param messageMapper  the mapper that converts concrete DTOs into a Message
     * @return a populated Message if the DTO type is supported; otherwise null
     */
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

    /**
     * Builds the template context map for a login notification email.
     * Keys included: firstName, deviceName, date, time, toEmail, and location (derived from city and country).
     *
     * @param loginDto login details used to populate the template variables
     * @return non-null map of template variables for the login email
     */
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
