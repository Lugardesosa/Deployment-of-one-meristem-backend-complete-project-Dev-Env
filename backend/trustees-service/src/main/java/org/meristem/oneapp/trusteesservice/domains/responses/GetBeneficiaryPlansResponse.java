package org.meristem.oneapp.trusteesservice.domains.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBeneficiaryPlansResponse {
    private String firstName;
    private String lastName;
    private String beneficiaryRelationship;
    private String gender;
    private String email;
    private String phoneNumber;
    private String dob;
    private String address;
    private String maritalStatus;
    private String accountDetails;
    private List<PlansMetainfo> details;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PlansMetainfo {

        private String metainfo;
    }
}
