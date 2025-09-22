package org.meristem.oneapp.usersservice.config.authConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.GeoIPDto;
import org.meristem.oneapp.kafka.dtos.LoginDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;
import org.meristem.oneapp.usersservice.domains.enums.MessageSubject;
import org.meristem.oneapp.usersservice.dtos.events.DeviceMetadataEvent;
import org.meristem.oneapp.usersservice.models.DeviceMetadata;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.services.KafkaSenderService;
import org.meristem.oneapp.usersservice.services.LoginService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessAuthenticationHandler implements AuthenticationSuccessHandler {


    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

    private Consumer<OAuth2AccessTokenAuthenticationContext> accessTokenResponseCustomizer;

    private final KafkaSenderService kafkaSenderService;

    private final LoginService loginService;

    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        writeAccessAndRefreshToken(response, authentication);
        notifyUser(request, response, authentication);
    }

    private void notifyUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        try {
            if (authentication.getPrincipal() instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
                if (usernamePasswordAuthenticationToken.getPrincipal() instanceof AuthenticatedUser users) {

//                    String ip = AppUtil.extractIp(request);
                    // TODO: DELETE THE BELOW LINE AND UNCOMMENT THE ABOVE LINE
                    String ip = "102.88.110.244";
                    GeoIPDto location = loginService.getLocation(ip);
                    String deviceDetails = loginService.getDeviceDetails(AppUtil.getUserAgent(request));

                    LocalDateTime localDateTime = LocalDateTime.now();
                    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

                    LoginDto loginDto = new LoginDto(MessageSubject.LOGIN_ALERT.getMessage(), new String[]{users.getEmail()}, users.getFirstName(), zonedDateTime.format(formatter), ip, deviceDetails,
                            location, false);
                    DeviceMetadata deviceMetadata = DeviceMetadata.builder().deviceDetails(deviceDetails)
                            .userId(users.getId()).lastLoggedIn(LocalDateTime.now()).location(loginService.formatLocation(location.cityName(), location.country())).build();
                    applicationEventPublisher.publishEvent(new DeviceMetadataEvent(this, deviceMetadata));
                    MessageDto messageDto = MessageDto.builder().medium(MessageMedium.EMAIL).type(MessageType.LOGIN_SUCCESSFUL).message(loginDto).classSimpleName(LoginDto.class.getSimpleName()).build();
                    kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_LOGIN_TOPIC));
                }
            }
        } catch(Exception e) {
            log.error("An error occurred verifying device or location");
            throw new RuntimeException(e);
        }
    }

    private void writeAccessAndRefreshToken(HttpServletResponse response, Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AccessTokenAuthenticationToken accessTokenAuthentication)) {
            if (log.isErrorEnabled()) {
                log.error("{} must be of type {} but was {}", Authentication.class.getSimpleName(), OAuth2AccessTokenAuthenticationToken.class.getName(), authentication.getClass().getName());
            }
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "Unable to process the access token response.", null);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType())
                .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }

        if (this.accessTokenResponseCustomizer != null) {
            // @formatter:off
            OAuth2AccessTokenAuthenticationContext accessTokenAuthenticationContext =
                    OAuth2AccessTokenAuthenticationContext.with(accessTokenAuthentication)
                            .accessTokenResponse(builder)
                            .build();
            // @formatter:on
            this.accessTokenResponseCustomizer.accept(accessTokenAuthenticationContext);
            if (log.isTraceEnabled()) {
                log.trace("Customized access token response");
            }
        }

        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.accessTokenResponseConverter.write(accessTokenResponse, null, httpResponse);
    }

    public void setAccessTokenResponseCustomizer(
            Consumer<OAuth2AccessTokenAuthenticationContext> accessTokenResponseCustomizer) {
        Assert.notNull(accessTokenResponseCustomizer, "accessTokenResponseCustomizer cannot be null");
        this.accessTokenResponseCustomizer = accessTokenResponseCustomizer;
    }

}
