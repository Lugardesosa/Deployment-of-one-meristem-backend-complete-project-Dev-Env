package org.meristem.oneapp.usersservice.config.authConfig;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * A custom implementation of the {@link Converter} interface that converts a {@link Jwt} object
 * into an {@link AbstractAuthenticationToken}. This class is used to extract roles and authorities
 * from a JWT and create an authentication token for use in Spring Security.
 *
 * <p>The class combines authorities extracted by the default {@link JwtGrantedAuthoritiesConverter}
 * with roles extracted from the "roles" claim in the JWT. Roles are prefixed with "ROLE_" to comply
 * with Spring Security conventions.</p>
 *
 * <p>Annotated with {@link Component} to allow Spring to manage its lifecycle and inject it where needed.</p>
 */
@Component
public class CustomJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {

        var authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(source).stream(),
                extractRoles(source).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(source, authorities, source.getClaimAsString(StandardClaimNames.SUB));
    }


    private Collection<? extends GrantedAuthority> extractRoles(Jwt source) {
        List<String> resource = source.getClaim("roles");
        if (resource == null) {
            return List.of();
        }
        return resource.stream()
                .map(s -> new SimpleGrantedAuthority("ROLE_".concat(s)))
                .collect(Collectors.toSet());

    }
}
