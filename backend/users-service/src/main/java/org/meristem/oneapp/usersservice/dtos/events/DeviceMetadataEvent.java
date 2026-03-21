package org.meristem.oneapp.usersservice.dtos.events;

import lombok.Getter;
import org.meristem.oneapp.usersservice.models.DeviceMetadata;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeviceMetadataEvent extends ApplicationEvent {

    private final DeviceMetadata deviceMetadata;

    public DeviceMetadataEvent(Object source, DeviceMetadata deviceMetadata) {
        super(source);
        this.deviceMetadata = deviceMetadata;
    }
}
