package org.meristem.oneapp.walletservice.config.AuthConfig;


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
