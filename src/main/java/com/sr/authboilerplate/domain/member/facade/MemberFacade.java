package com.sr.authboilerplate.domain.member.facade;

import com.sr.authboilerplate.domain.member.api.request.SignUpRequest;
import com.sr.authboilerplate.domain.member.api.response.SignUpResponse;
import com.sr.authboilerplate.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        Long memberId = memberService.join(request.email(), request.password());

        return SignUpResponse.of(memberId);
    }
    
}
