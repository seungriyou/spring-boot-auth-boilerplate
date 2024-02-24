package com.sr.authboilerplate.domain.auth.api.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
    public static AuthResponse of(String accessToken, String refreshToken, String tokenType) {
        return new AuthResponse(accessToken, refreshToken, tokenType);
    }
}
