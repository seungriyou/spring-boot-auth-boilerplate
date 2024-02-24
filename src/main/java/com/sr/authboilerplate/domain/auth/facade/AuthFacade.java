package com.sr.authboilerplate.domain.auth.facade;

import static com.sr.authboilerplate.global.exception.ErrorCode.FAILED_LOGIN_BY_ANYTHING;

import com.sr.authboilerplate.domain.auth.api.request.LogInRequest;
import com.sr.authboilerplate.domain.auth.api.request.TokenReissueRequest;
import com.sr.authboilerplate.domain.auth.api.response.AuthResponse;
import com.sr.authboilerplate.domain.auth.api.response.TokenReissueResponse;
import com.sr.authboilerplate.domain.auth.entity.Auth;
import com.sr.authboilerplate.domain.auth.service.AuthService;
import com.sr.authboilerplate.domain.member.entity.Member;
import com.sr.authboilerplate.domain.member.service.MemberService;
import com.sr.authboilerplate.global.auth.jwt.JwtProvider;
import com.sr.authboilerplate.global.auth.jwt.dto.TokenDto;
import com.sr.authboilerplate.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;


    @Transactional
    public AuthResponse login(LogInRequest request) {
        // 1. email로 존재하는 회원 찾기
        // email과 password 관련 로그인 실패 에러를 하나로 통합하여 예외 처리
        // Member member = memberService.getMemberByEmail(request.email());
        Member member;
        try {
            member = memberService.getMemberByEmail(request.email());
        } catch (CustomException e) {
            throw new CustomException(FAILED_LOGIN_BY_ANYTHING);
        }

        // 2. password 검증
        authService.checkPassword(request.password(), member.getPassword());

        // 3. token 발급
        TokenDto token = authService.createTokenAndSaveAuth(member.getId());
        return AuthResponse.of(token.accessToken(), token.refreshToken(), token.tokenType());
    }

    @Transactional
    public void logout(Long memberId) {
        // DB에서 refresh token(Auth) 제거
        authService.deleteRefreshToken(memberId);
    }

    public TokenReissueResponse generateAccessTokenByRefreshToken(TokenReissueRequest request) {
        // 1. refresh token 검증
        jwtProvider.validateToken(request.refreshToken());

        // 2. DB에 저장된 Auth 찾기
        Auth auth = authService.getAuthByRefreshToken(request.refreshToken());

        // 3. DB에 Auth에 해당하는 member가 존재하는지 다시 확인
        Long memberId = memberService.getMemberById(auth.getMemberId()).getId();

        // 4. access token 재발급
        String accessToken = jwtProvider.createAccessToken(memberId);

        return TokenReissueResponse.of(accessToken);
    }

}
