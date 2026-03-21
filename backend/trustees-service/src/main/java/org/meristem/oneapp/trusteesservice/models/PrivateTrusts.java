package org.meristem.oneapp.trusteesservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.meristem.oneapp.trusteesservice.validations.constraints.ExistsById;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Table("private_trusts")
public class PrivateTrusts extends BaseModel<String> {

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 150)
    private String lastName;

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 150)
    private String firstName;

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 300)
    private String email;

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 50)
    private String phoneNumber;

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 400)
    private String address;

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 50)
    private String title;

    @NotNull(message = "Not null")
    private Integer objective;

    @NotNull(message = "Not null")
    private Integer frequency;

    @NotNull(message = "Not null")
    private LocalDate commencementDate;

    @NotNull(message = "Not null")
    private LocalDate terminationDate;

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 500)
    private String powerOfTrustee;

    @NotNull(message = "Currency assetType cannot be blank")
    @ExistsById(message = "Currency does not exist", tableName = Currencies.class)
    private Long currencyId;

    @NotNull(message = "Estimated amount cannot be null")
    private BigDecimal fundContribution;

    @NotNull(message = "Not null")
    private Long ownerId;

    @NotNull(message = "Not null")
    private String metainfo;

    @Builder
    public PrivateTrusts(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String lastName, String firstName, String email, String phoneNumber, String address, String title, Integer objective, Integer frequency, LocalDate commencementDate, LocalDate terminationDate, String powerOfTrustee, Long ownerId, Long currencyId, BigDecimal fundContribution, String metainfo) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.title = title;
        this.objective = objective;
        this.frequency = frequency;
        this.commencementDate = commencementDate;
        this.terminationDate = terminationDate;
        this.powerOfTrustee = powerOfTrustee;
        this.currencyId = currencyId;
        this.fundContribution = fundContribution;
        this.ownerId = ownerId;
        this.metainfo = metainfo;
    }
}
