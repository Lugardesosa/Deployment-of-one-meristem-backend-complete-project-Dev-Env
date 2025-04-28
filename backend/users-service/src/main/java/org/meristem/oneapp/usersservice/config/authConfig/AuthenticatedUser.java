package org.meristem.oneapp.usersservice.config.authConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthenticatedUser.class, name = "authenticatedUser")
})
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
public class AuthenticatedUser implements UserDetails {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String password;
    private String phoneNumber;
    private int status;
    private List<GrantedAuthority> authorities;

    public AuthenticatedUser(Long id, String email, String firstName, String lastName, String middleName, String password, String phoneNumber, List<GrantedAuthority> authorities, int status) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
        this.status = status;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return "";
    }
}
