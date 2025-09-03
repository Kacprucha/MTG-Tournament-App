package com.example.backend.seciurity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeycloakJwtTokenConverter implements Converter<Jwt, AbstractAuthenticationToken>
{
    private final KeycloakJwtAuthoritiesConverter keycloakJwtAuthoritiesConverter;

    @Override
    public AbstractAuthenticationToken convert (@NonNull Jwt jwt) 
    {
        return new JwtAuthenticationToken (jwt, keycloakJwtAuthoritiesConverter.convert(jwt),
            getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName (Jwt jwt) 
    {
        return jwt.getClaimAsString("preferred_username");
    }
}
