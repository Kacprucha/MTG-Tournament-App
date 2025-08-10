package com.example.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JwtConfig 
{
    @Value("${spring.security.oauth2.resource-server.jwt.issuer-uri}")
    private String jwkSerUri;

    // @Bean
    // public JwtDecoder jwtDecoder() 
    // {
    //     log.info("Configuring JWT Decoder with issuer URI: {}", jwkSerUri);
    //     NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwtSerUri).build();

    //     OAuth2TokenValidator<Jwt> validator = JwtValidators.createDefault();
    //     jwtDecoder.setJwtValidator(validator);

    //     return jwtDecoder;
    // }

    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() 
    {
        return new JwtGrantedAuthoritiesConverter();
    }
}
