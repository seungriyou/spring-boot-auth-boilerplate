package com.sr.authboilerplate.domain.auth.api.response;

public record TokenReissueResponse(
        String accessToken
) {

    public static TokenReissueResponse of(String accessToken) {
        return new TokenReissueResponse(accessToken);
    }
}
