package com.jumia.OrderService.external.intercept;

import com.jumia.OrderService.service.TokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
public class OAuthRequestInterceptor implements RequestInterceptor {

    private final TokenService tokenService;

    public OAuthRequestInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Override
    public void apply(RequestTemplate template) {
        String token = tokenService.extractToken();
        if (token != null) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}
