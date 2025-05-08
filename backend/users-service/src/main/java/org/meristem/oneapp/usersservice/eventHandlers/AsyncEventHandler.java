package org.meristem.oneapp.usersservice.eventHandlers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.Roles;
import org.meristem.oneapp.usersservice.domains.enums.EntityStatus;
import org.meristem.oneapp.usersservice.dtos.events.RequestAndResponseLogEvent;
import org.meristem.oneapp.usersservice.dtos.events.UserOnboardingCompletionEvent;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncEventHandler {

    private final UserProfileRepository profileRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserSignUpCompletionEvent(UserOnboardingCompletionEvent event) {

        try {

            UserProfile profile = UserProfile.builder().userId(event.getUserId()).referralCode(AppUtil.generateReferralCode(event.getFirstName())).build();

            profileRepository.save(profile);
            requirementsRepository.findAllByStatus(EntityStatus.ACTIVE.getValue())
                    .forEach(rId -> {
                        UserOnboarding userOnboarding = UserOnboarding.builder()
                                .completed(false).userId(event.getUserId()).requirementId(rId).build();
                        userOnboardingRepository.save(userOnboarding);
                    });
            usersRepository.saveRole(event.getUserId(), rolesRepository.findIdByName(Roles.USER.getName()));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
