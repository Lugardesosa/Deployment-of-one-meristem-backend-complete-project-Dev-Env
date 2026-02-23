package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressResponse{

    private String houseAddress;
    private String city;
    private String street;
    private String number;
    private String state;
    private Long countryId;
    private String landmark;
    private String zipOrPostalCode;
    private Long userId;
    private Integer verificationMethod;
    private Integer utilityBillType;
    @JsonProperty("documentSignedUrl")
    private String documentKey;
}
