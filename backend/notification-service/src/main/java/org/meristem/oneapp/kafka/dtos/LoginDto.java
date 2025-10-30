package org.meristem.oneapp.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {

    private String subject;
    private String[] recipients;
    private String firstname;
    private String date;
    private String time;
    private String ipAddress;
    private String deviceDetails;
    private GeoIPDto location;
    private boolean isDeviceNew;
}