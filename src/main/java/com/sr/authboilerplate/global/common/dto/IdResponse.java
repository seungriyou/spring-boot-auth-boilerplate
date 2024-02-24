package com.sr.authboilerplate.global.common.dto;

public record IdResponse(
        Long id
) {
    public static IdResponse of(Long id) {
        return new IdResponse(id);
    }
}
