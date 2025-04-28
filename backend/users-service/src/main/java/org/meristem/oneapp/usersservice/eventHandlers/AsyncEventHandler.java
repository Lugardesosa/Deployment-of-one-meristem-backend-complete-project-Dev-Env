package org.meristem.oneapp.usersservice.eventHandlers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;
import org.meristem.oneapp.usersservice.domains.enums.Roles;
import org.meristem.oneapp.usersservice.domains.enums.Status;
import org.meristem.oneapp.usersservice.dtos.events.UserOnboardingCompletionEvent;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.repositories.*;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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

        UserProfile profile = UserProfile.builder().userId(event.getUserId()).build();
        profileRepository.save(profile);
        requirementsRepository.findAllByStatus(Status.ACTIVE.getValue())
                .forEach(rId -> {
                    UserOnboarding userOnboarding = UserOnboarding.builder()
                            .completed(false).userId(event.getUserId()).requirementId(rId).build();
                    userOnboardingRepository.save(userOnboarding);
                });
        usersRepository.saveRole(event.getUserId(), rolesRepository.findIdByName(Roles.USER.getName()));
    }
}
