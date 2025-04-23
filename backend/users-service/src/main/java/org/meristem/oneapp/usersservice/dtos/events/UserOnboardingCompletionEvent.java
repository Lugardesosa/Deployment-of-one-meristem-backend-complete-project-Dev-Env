package org.meristem.oneapp.usersservice.dtos.events;

import lombok.Getter;
import org.meristem.oneapp.usersservice.domains.requests.SubmitOnboardingRequest;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserOnboardingCompletionEvent extends ApplicationEvent {

    private final SubmitOnboardingRequest object;

    public UserOnboardingCompletionEvent(Object source, SubmitOnboardingRequest object) {
        super(source);
        this.object = object;
    }
}
