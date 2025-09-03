package com.example.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.example.backend.seciurity.KeycloakJwtAuthoritiesConverter;
import com.example.backend.seciurity.KeycloakJwtTokenConverter;

@Configuration
public class AuthConverterConfig 
{
    @Bean
    public KeycloakJwtTokenConverter keycloakJwtTokenConverter(
        KeycloakJwtAuthoritiesConverter authoritiesConverter 
    ) 
    {
        return new KeycloakJwtTokenConverter(authoritiesConverter);
    }


    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() 
    {
        return new JwtGrantedAuthoritiesConverter();
    }
}
