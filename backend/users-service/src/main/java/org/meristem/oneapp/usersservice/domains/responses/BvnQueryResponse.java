package org.meristem.oneapp.usersservice.domains.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BvnQueryResponse implements Serializable {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String bvn;
    private String bvnHashed;
    private boolean emailVerified;
    private boolean passwordSet;
}
