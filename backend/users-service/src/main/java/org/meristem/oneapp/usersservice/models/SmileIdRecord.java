package org.meristem.oneapp.usersservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("smile_id_record")
public class SmileIdRecord extends BaseModel<String> {

    /**
     * The unique identifier for the Smile ID job.
     * Must not be blank and cannot exceed 100 characters.
     */
    @Size(max = 100, message = "Not more than 100")
    @NotBlank(message = "jobId cannot be blank")
    @Column("job_id")
    private String jobId;

    /**
     * The user ID associated with the Smile ID job.
     * Must not be blank and cannot exceed 150 characters.
     */
    @Size(max = 150, message = "Not more than 150")
    @NotBlank(message = "userId cannot be blank")
    @Column( "user_id")
    private String userId;

    /**
     * The message of the job.
     * Must not be blank and cannot exceed 300 characters.
     */
    @Size(max = 300, message = "Not more than 300")
    @NotBlank(message = "message cannot be blank")
    @Column("message")
    private String message;

    /**
     * The product type associated with the Smile ID job.
     * Must not be null.
     */
    @NotNull(message = "jobType cannot be null")
    @Column("job_type")
    private Integer jobType;

    /**
     * The requirement id associated with the Smile ID job.
     */
    @NotNull(message = "requirementId cannot be null")
    @Column("requirement_id")
    private Long requirementId;


    /**
     * Constructs a new SmileIdRecord instance with the specified details.
     *
     * @param id the ID of the record
     * @param createdDate the date the record was created
     * @param createdBy the user who created this record
     * @param lastModifiedDate the date the record was last modified
     * @param lastModifiedBy the user who last modified this record
     * @param version the version of the record
     * @param jobId the job ID of the Smile ID record
     * @param userId the user ID associated with the Smile ID record
     * @param jobType the product type of the Smile ID record
     * @param requirementId the requirement id associated with this job
     */

    @Builder
    public SmileIdRecord(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String jobId, String userId,
                         Integer jobType, Long requirementId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.jobId = jobId;
        this.userId = userId;
        this.jobType = jobType;
        this.requirementId = requirementId;
    }

    /**
     * Checks if this SmileIdRecord is equal to another object.
     * Two records are considered equal if they have the same job ID.
     *
     * @param o the object to compare to
     * @return true if the records are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SmileIdRecord that = (SmileIdRecord) o;
        return Objects.equals(getJobId(), that.getJobId());
    }

    /**
     * Returns a hash code value for the SmileIdRecord.
     * The hash code is based on the job ID.
     *
     * @return a hash code value for this record
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getJobId());
    }

}