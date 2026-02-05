package com.jumia.CloudGateway.controller;

import com.jumia.CloudGateway.model.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private final ReactiveOAuth2AuthorizedClientService clientService;

    public AuthenticationController(ReactiveOAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @AuthenticationPrincipal OidcUser oidcUser,
            @RegisteredOAuth2AuthorizedClient("okta")
            OAuth2AuthorizedClient client
    ) {

        AuthenticationResponse authenticationResponse =
                new AuthenticationResponse(
                        oidcUser.getEmail(),
                        client.getAccessToken().getTokenValue(),
                        client.getRefreshToken() != null
                                ? client.getRefreshToken().getTokenValue()
                                : null,
                        client.getAccessToken().getExpiresAt().getEpochSecond(),
                        oidcUser.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                );

        return ResponseEntity.ok(authenticationResponse);
    }

    @GetMapping("/print-token")
    public Mono<String> printToken(Principal principal) {
        return clientService.loadAuthorizedClient("auth0", principal.getName())
                .map(client -> {
                    String accessToken = client.getAccessToken().getTokenValue();
                    System.out.println("Access Token: " + accessToken);
                    return accessToken;
                })
                .defaultIfEmpty("No access token found.");
    }
}
