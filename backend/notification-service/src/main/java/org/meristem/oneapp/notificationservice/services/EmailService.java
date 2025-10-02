package org.meristem.oneapp.notificationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.dtos.messaging.Message;
import org.meristem.oneapp.notificationservice.mappers.MessageDtoToMessageMapper;
import org.meristem.oneapp.notificationservice.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

import static java.util.Objects.nonNull;

@Slf4j
@Service("EMAIL")
@RequiredArgsConstructor
public class EmailService implements NotificationService<MessageDto> {

    @Value("${one-app.notification-service.mail-sender}")
    public String FROM;
    @Value("${one-app.support.email}")
    public String SUPPORT_EMAIL;
    @Value("${one-app.support.phone}")
    public String SUPPORT_PHONE;
    @Value("${one-app.logo.green-url}")
    private String MERISTEM_GREEN_LOGO;

    private final JavaMailSender mailSender;
    private final MessageDtoToMessageMapper messageMapper = MessageDtoToMessageMapper.INSTANCE;
    private final ObjectMapper mapper;
    private final TemplateEngine templateEngine;


    @Override
    public void send(MessageDto request) {
        Message message = unbox(request, mapper, messageMapper);
        sendMail(message, request.isHtml());
    }

    public void sendMail(Message messageDetails, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, isHtml, Charset.defaultCharset().displayName());

            String body = messageDetails.getBody();
            if (isHtml) {
                Context context = new Context();
                context.setVariables(messageDetails.getContext());
                setContext(context);
                body = templateEngine.process(messageDetails.getEmailTemplate().getFileName(), context);
            }
            helper.setFrom(FROM);
            helper.setTo(messageDetails.getRecipient());
            helper.setSubject(messageDetails.getSubject());
            helper.setText(body, isHtml);
            if (nonNull(messageDetails.getCc())) {
                helper.setCc(messageDetails.getCc());
            }
            for (Map.Entry<String, File> entry : messageDetails.getFiles().entrySet()) {
                helper.addAttachment(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, InputStreamSource> entry : messageDetails.getInputStreamSourceMap().entrySet()) {
                helper.addAttachment(entry.getKey(), entry.getValue());
            }
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email with subject {} to email {}", messageDetails.getSubject(), messageDetails.getRecipient(), e);
        }
    }

    private void setContext(Context context) {
        context.setVariable("MERISTEM_GREEN_LOGO", MERISTEM_GREEN_LOGO);
        context.setVariable("SUPPORT_EMAIL", SUPPORT_EMAIL);
        context.setVariable("SUPPORT_PHONE", SUPPORT_PHONE);
    }
}
