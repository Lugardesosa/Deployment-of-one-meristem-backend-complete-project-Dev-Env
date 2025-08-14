package org.meristem.oneapp.coreservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class Wills extends BaseModel<String> {

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 150)
    private String lastName;

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 150)
    private String firstName;

    @Size(min = 1, max = 150)
    private String middleName;

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

    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 50)
    private String maritalStatus;

    @NotNull(message = "Not null")
    private Long ownerId;

    public Wills(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String lastName, String firstName, String middleName, String email, String phoneNumber, String address, String title, String maritalStatus, Long ownerId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.title = title;
        this.maritalStatus = maritalStatus;
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Wills that = (Wills) o;
        return Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getId());
    }
}
