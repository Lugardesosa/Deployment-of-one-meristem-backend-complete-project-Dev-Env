package org.meristem.oneapp.usersservice.dtos.events;

import lombok.Getter;
import org.meristem.oneapp.usersservice.domains.requests.SubmitOnboardingRequest;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserOnboardingCompletionEvent extends ApplicationEvent {

    private final Long userId;
    private final String firstName;

    public UserOnboardingCompletionEvent(Object source, Long userId, String firstName) {
        super(source);
        this.userId = userId;
        this.firstName = firstName;
    }
}
