package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meristem.oneapp.usersservice.domains.enums.Gender;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BvnQueryResponse implements Serializable {

    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String email;
    private String bvn;
    private String bvnHashed;
    private Boolean emailVerified;
    private Boolean passwordSet;
    private Boolean success;
    private String message;
    @JsonIgnore
    private String gender;
    @JsonIgnore
    private LocalDate dob;
}
