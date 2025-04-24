package org.meristem.oneapp.usersservice.eventHandlers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.dtos.events.UserOnboardingCompletionEvent;
import org.meristem.oneapp.usersservice.repositories.UserOnboardingRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Async
@RequiredArgsConstructor
public class AsyncEventHandler {


}
