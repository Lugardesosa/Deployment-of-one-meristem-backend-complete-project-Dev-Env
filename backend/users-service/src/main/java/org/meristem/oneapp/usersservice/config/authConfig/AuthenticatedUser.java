package org.meristem.oneapp.usersservice.config.authConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    private String middlewareCustomerId;
    private int status;
    private List<GrantedAuthority> authorities;
    private int passwordAttempt;
    private boolean emailVerified;
    private Integer accountType;

    public AuthenticatedUser(String middlewareCustomerId, Long id, String email, String firstName, String lastName, String middleName, String password, String phoneNumber, List<GrantedAuthority> authorities, int status, int passwordAttempt, boolean emailVerified, Integer accountType) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
        this.status = status;
        this.passwordAttempt = passwordAttempt;
        this.emailVerified = emailVerified;
        this.accountType = accountType;
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
        return this.email;
    }
}
