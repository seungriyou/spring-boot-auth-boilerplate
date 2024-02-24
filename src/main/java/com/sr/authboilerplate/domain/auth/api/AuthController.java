package com.sr.authboilerplate.domain.auth.api;

import com.sr.authboilerplate.domain.auth.api.request.LogInRequest;
import com.sr.authboilerplate.domain.auth.api.request.TokenReissueRequest;
import com.sr.authboilerplate.domain.auth.api.response.AuthResponse;
import com.sr.authboilerplate.domain.auth.api.response.TokenReissueResponse;
import com.sr.authboilerplate.domain.auth.facade.AuthFacade;
import com.sr.authboilerplate.global.auth.jwt.dto.MemberDetails;
import com.sr.authboilerplate.global.common.dto.IdResponse;
import com.sr.authboilerplate.global.common.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "02. Auth Controller", description = "인증 인가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthFacade authFacade;
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final Integer COOKIE_EXPIRATION_SECONDS = 7 * 24 * 60 * 60;


    @Operation(summary = "로그인", description = "email과 password를 이용하여 로그인을 진행합니다.")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<AuthResponse>> login(@Valid @RequestBody LogInRequest request) {
        // get toekn
        AuthResponse response = authFacade.login(request);

        // access / refresh token을 넘겨주는 방법: (1) response body (2) set-cookie header (w/ http only, secure option)
        // set-cookie header: 클라이언트의 쿠키에 refresh token 저장

        // 우선은, 두 방법 모두 적용

        // create token cookies
        ResponseCookie accessTokenCookie = createTokenCookie(ACCESS_TOKEN, response.accessToken());
        ResponseCookie refreshTokenCookie = createTokenCookie(REFRESH_TOKEN, response.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new SuccessResponse<>(response));
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<IdResponse> logout(@AuthenticationPrincipal MemberDetails memberDetails) {
        authFacade.logout(memberDetails.memberId());
        return ResponseEntity.ok().body(new IdResponse(memberDetails.memberId()));
    }

    @Operation(summary = "액세스 토큰 재발급", description = "Refresh Token을 통해 Access Token을 재발급합니다.")
    @PostMapping("/reissue-token")
    public ResponseEntity<SuccessResponse<TokenReissueResponse>> reissueAccessToken(@Valid @RequestBody TokenReissueRequest request) {
        TokenReissueResponse response = authFacade.generateAccessTokenByRefreshToken(request);
        return ResponseEntity.ok().body(new SuccessResponse<>(response));
    }


    @Operation(summary = "로그인 정보 확인", description = "현재 로그인 되어 있는 회원의 식별자 값을 반환합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<IdResponse> getInfo(@AuthenticationPrincipal MemberDetails memberDetails) {
        Long memberId = memberDetails.memberId();
        return ResponseEntity.ok().body(new IdResponse(memberId));
    }

    private ResponseCookie createTokenCookie(String tokenType, String token) {
        return ResponseCookie.from(tokenType, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(COOKIE_EXPIRATION_SECONDS)
                .sameSite("None")
                .build();
    }

}
