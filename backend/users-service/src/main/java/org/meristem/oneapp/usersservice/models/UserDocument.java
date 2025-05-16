package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This is the user document entity, it contains all the documents the given user has provided.
 * the user uploads a document and the url is persisted in this table.
 */
@NoArgsConstructor
@Setter
@Getter
@Table("user_document")
public class UserDocument extends BaseModel<String> {

    @Size(max = 50)
    @NotBlank(message = "Not null")
    private String idType;

    @Size(max = 1000)
    @NotBlank(message = "Not null")
    @Column("additional_url")
    private String additionalUrl;

    @Size(max = 1000)
    @NotBlank(message = "Not null")
    @Column("id_card_front_url")
    private String idCardFront;

    @Size(max = 1000)
    @NotBlank(message = "Not null")
    @Column("id_card_back_url")
    private String idCardBack;

    @Size(max = 1000)
    @NotBlank(message = "Not null")
    @Column("selfie_image_url")
    private String selfieImage;

    @NotNull(message = "Not null")
    private Long requirementId;

    @NotNull(message = "Not null")
    private Long userId;

    /**
     * Constructs a new UserDocument with the specified details.
     *
     * @param id the ID of the document
     * @param createdDate the date the document was created
     * @param createdBy the user who created the document
     * @param lastModifiedDate the date the document was last modified
     * @param lastModifiedBy the user who last modified the document
     * @param version the version of the document
     * @param idType the name of the document
     * @param requirementId the ID of the requirement associated with the document
     * @param userId the ID of the user who uploaded the document
     * @param additionalUrl the url of the kyc receipt
     * @param idCardFront the url of the front of the id card
     * @param idCardBack the url of the back of the id card
     * @param selfieImage the url of the selfie of the user
     */
    @Builder
    public UserDocument(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                        String idType, Long requirementId, Long userId, String additionalUrl, String idCardFront, String idCardBack, String selfieImage) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.idType = idType;
        this.requirementId = requirementId;
        this.userId = userId;
        this.idCardFront = idCardFront;
        this.idCardBack = idCardBack;
        this.selfieImage = selfieImage;
        this.additionalUrl = additionalUrl;
    }

    /**
     * Checks if this UserDocument is equal to another object.
     * Two UserDocuments are considered equal if they have the same requirementId and userId.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDocument that = (UserDocument) o;
        return Objects.equals(getRequirementId(), that.getRequirementId()) && Objects.equals(getUserId(), that.getUserId());
    }

    /**
     * Returns a hash code value for the object.
     * The hash code is based on the requirementId and userId.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(getRequirementId(), getUserId());
    }
}
