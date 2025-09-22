package org.meristem.oneapp.usersservice.events;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.dtos.events.DeviceMetadataEvent;
import org.meristem.oneapp.usersservice.dtos.events.RequestAndResponseLogEvent;
import org.meristem.oneapp.usersservice.models.DeviceMetadata;
import org.meristem.oneapp.usersservice.services.LoginService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncEventHandler {

    private final LoginService loginService;

    @EventListener
    @Async
    public void saveDeviceInfo(DeviceMetadataEvent event) {

        loginService.saveDevice(event.getDeviceMetadata());

    }
}
