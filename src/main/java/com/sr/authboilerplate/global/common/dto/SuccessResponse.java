package com.sr.authboilerplate.global.common.dto;

public record SuccessResponse<T>(
        T data
) {

}
