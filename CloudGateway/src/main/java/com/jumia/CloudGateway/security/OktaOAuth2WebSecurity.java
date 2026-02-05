package com.jumia.CloudGateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class OktaOAuth2WebSecurity {

    @Value("${auth0.audience}")
    private String audience;


    private final ReactiveClientRegistrationRepository repository;
    public OktaOAuth2WebSecurity(ReactiveClientRegistrationRepository repository) {
        this.repository = repository;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(
                        exchanges -> exchanges
                                .anyExchange()
                                .authenticated()
                )
                .oauth2Login(oAuth2LoginSpec -> oAuth2LoginSpec
                        .authorizationRequestResolver(authorizationRequestResolver(repository)))
                .oauth2ResourceServer(oauth2->oauth2
                        .jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
        ;
        return http.build();
    }

    private DefaultServerOAuth2AuthorizationRequestResolver authorizationRequestResolver(ReactiveClientRegistrationRepository repository) {
        DefaultServerOAuth2AuthorizationRequestResolver resolver = new DefaultServerOAuth2AuthorizationRequestResolver(
                repository);
        resolver.setAuthorizationRequestCustomizer(customizer());
        return resolver;
    }

    private Consumer<OAuth2AuthorizationRequest.Builder> customizer() {
        return customizer -> {
            customizer.additionalParameters(params -> params.put("audience", audience));
        };
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt->{
            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);
            Collection<GrantedAuthority> customAuthorities = jwt.getClaimAsStringList("https://jumia.com/roles")
                    .stream().map(role->new SimpleGrantedAuthority("ROLE_"+role))
                    .collect(Collectors.toList());
            authorities.addAll(customAuthorities);
            return authorities;
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
