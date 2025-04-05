package org.meristem.oneapp.usersservice.domains.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserResponse {

    Long id;
    LocalDateTime createdDate;
    String createdBy;
    LocalDateTime lastModifiedDate; String lastModifiedBy; String email;
    String firstName; String lastName; String middleName; String password; String phoneNumber; String referralCode;
}
