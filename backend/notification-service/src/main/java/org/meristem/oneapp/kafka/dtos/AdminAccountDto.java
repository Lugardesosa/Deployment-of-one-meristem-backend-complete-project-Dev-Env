package org.meristem.oneapp.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccountDto {

    private String[] recipient;
    private String subject;
    private String body;
    private String firstName;

}
