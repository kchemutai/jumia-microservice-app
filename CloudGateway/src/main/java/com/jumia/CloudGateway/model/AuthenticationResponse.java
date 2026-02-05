package com.jumia.CloudGateway.model;

import java.util.Collection;

public record AuthenticationResponse(
        String userId,
        String accessToken,
        String refreshToken,
        long expiresAt,
        Collection<String> authorityList
) {}
