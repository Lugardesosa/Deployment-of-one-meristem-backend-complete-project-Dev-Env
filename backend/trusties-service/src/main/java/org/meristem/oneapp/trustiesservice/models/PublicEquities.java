package org.meristem.oneapp.trustiesservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Table("public_equities")
@Setter
@Getter
public class PublicEquities extends Assets {

    @NotBlank(message = "Cannot be blank")
    @Size(max = 50, message = "Cannot be more than 50 chars")
    @Column("company_type")
    private String companyType;

    @NotBlank(message = "Cannot be blank")
    @Size(max = 150, message = "Cannot be more than 150 chars")
    @Column("brokerage_house")
    private String brokerageHouse;

    @NotBlank(message = "Cannot be blank")
    @Size(max = 150, message = "Cannot be more than 150 chars")
    @Column("share_name")
    private String shareName;

    @NotBlank(message = "Cannot be blank")
    @Size(max = 50, message = "Cannot be more than 50 chars")
    @Column("cscs_number")
    private String cscsNumber;

    @NotBlank(message = "Cannot be blank")
    @Size(max = 50, message = "Cannot be more than 50 chars")
    @Column("chn")
    private String chn;

    @NotNull(message = "Cannot be blank")
    @Column("no_of_units")
    private Integer noOfUnits;

    @Builder
    public PublicEquities(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String companyType, String brokerageHouse, String shareName, String cscsNumber, String chn, Integer noOfUnits) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.companyType = companyType;
        this.brokerageHouse = brokerageHouse;
        this.shareName = shareName;
        this.cscsNumber = cscsNumber;
        this.chn = chn;
        this.noOfUnits = noOfUnits;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicEquities that = (PublicEquities) o;
        return Objects.equals(getShareName(), that.getShareName()) && Objects.equals(getCscsNumber(), that.getCscsNumber()) && Objects.equals(getChn(), that.getChn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShareName(), getCscsNumber(), getChn());
    }
}
