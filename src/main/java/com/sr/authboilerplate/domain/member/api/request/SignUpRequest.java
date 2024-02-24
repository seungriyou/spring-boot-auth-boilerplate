package com.sr.authboilerplate.domain.member.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank(message = "email은 blank일 수 없습니다.")
        @Email
        String email,

        @NotBlank(message = "password는 blank일 수 없습니다.")
        String password
) {

}
