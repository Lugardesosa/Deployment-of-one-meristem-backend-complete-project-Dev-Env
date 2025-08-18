package org.meristem.oneapp.trustiesservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.meristem.oneapp.trustiesservice.domains.enums.MarriageType;
import org.meristem.oneapp.trustiesservice.domains.enums.YesOrNo;
import org.meristem.oneapp.trustiesservice.validations.constraints.ContainsEnum;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Table("comprehensive_will")
public class ComprehensiveWill extends Wills {

    @NotBlank(message = "Not blank")
    @ContainsEnum(enumClass = MarriageType.class)
    private String marriageType;

    @NotBlank(message = "Not blank")
    @ContainsEnum(enumClass = MarriageType.class)
    private String religion;

    @Size(min = 1, max = 100)
    @NotBlank(message = "Not blank")
    private String occupation;

    @NotBlank(message = "Not blank")
    @ContainsEnum(enumClass = YesOrNo.class)
    private String customaryTradition;

    private String traditionDetails;

    private String otherDetails;

    @Builder
    public ComprehensiveWill(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String lastName, String firstName, String middleName, String email, String phoneNumber, String address, String title, String maritalStatus, Long ownerId, String marriageType, String religion, String occupation, String customaryTradition, String traditionDetails, String otherDetails) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, lastName, firstName, middleName, email, phoneNumber, address, title, maritalStatus, ownerId);
        this.marriageType = marriageType;
        this.religion = religion;
        this.occupation = occupation;
        this.customaryTradition = customaryTradition;
        this.traditionDetails = traditionDetails;
        this.otherDetails = otherDetails;
    }
}
