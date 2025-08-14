package org.meristem.oneapp.coreservice.domains.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PlanResponse {

    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

    private String email;

    private String phoneNumber;

    private String address;

    private Long ownerId;

    private Set<PlanAssetResponse> assets;

    @Builder.Default
    private Set<PlanBeneficiariesResponse> beneficiaries =  new HashSet<>();
}
