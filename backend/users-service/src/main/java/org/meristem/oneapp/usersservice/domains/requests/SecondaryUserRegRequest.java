package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

@Builder
public record SecondaryUserRegRequest(@NotBlank(message = "Cannot be blank") @Schema(description = "email address") String email, @NotBlank(message = "Cannot be blank") @Schema(description = "key from email") String key, @Hidden @JsonIgnore Long userId) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SecondaryUserRegRequest request = (SecondaryUserRegRequest) o;
        return Objects.equals(key(), request.key()) && Objects.equals(email(), request.email());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email(), key());
    }
}
