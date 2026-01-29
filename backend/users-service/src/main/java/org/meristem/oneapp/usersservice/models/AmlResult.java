package org.meristem.oneapp.usersservice.models;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
@Table("aml_result")
@ToString
public class AmlResult extends BaseModel<String> {

    private Long searchId;

    private String vendorId;

    private String name;

    private String dobs;

    private String gender;

    // comma separated
    private String entityType;

    // comma separated
    private String countries;

    // comma separated
    private String aliases;

    private Boolean pep;

    private Boolean sanctioned;

    private String photo;

    // comma separated
    private String politicalParty;

    private JsonNode education;

    private JsonNode position;


    private Integer confidenceScore;

    private JsonNode vendorDataset;

    @Builder
    public AmlResult(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long searchId, String vendorId, String name, String dobs, String gender, String entityType, String countries, String aliases, Boolean pep, Boolean sanctioned, String photo, String politicalParty, JsonNode education, JsonNode position, Integer confidenceScore, JsonNode vendorDataset) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.searchId = searchId;
        this.vendorId = vendorId;
        this.name = name;
        this.dobs = dobs;
        this.gender = gender;
        this.entityType = entityType;
        this.countries = countries;
        this.aliases = aliases;
        this.pep = pep;
        this.sanctioned = sanctioned;
        this.photo = photo;
        this.politicalParty = politicalParty;
        this.education = education;
        this.position = position;
        this.confidenceScore = confidenceScore;
        this.vendorDataset = vendorDataset;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AmlResult amlResult = (AmlResult) o;
        return Objects.equals(getSearchId(), amlResult.getSearchId()) && Objects.equals(getVendorId(), amlResult.getVendorId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSearchId(), getVendorId());
    }
}
