package org.meristem.oneapp.usersservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Table("device_metadata")
public class DeviceMetadata extends BaseModel<String> {

    private Long userId;
    private String deviceDetails;
    private String location;
    private LocalDateTime lastLoggedIn;


    @Builder
    public DeviceMetadata(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, String deviceDetails, String location, LocalDateTime lastLoggedIn) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.deviceDetails = deviceDetails;
        this.location = location;
        this.lastLoggedIn = lastLoggedIn;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        DeviceMetadata that = (DeviceMetadata) o;
//        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getDeviceDetails(), that.getDeviceDetails()) && Objects.equals(getLocation(), that.getLocation());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getUserId(), getDeviceDetails(), getLocation());
//    }

}
