package com.sr.authboilerplate.domain.member.api.response;

public record SignUpResponse(
        Long memberId
) {
    public static SignUpResponse of(Long memberId) {
        return new SignUpResponse(memberId);
    }
}
