package com.sr.authboilerplate.domain.member.service;


import static com.sr.authboilerplate.global.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.sr.authboilerplate.global.exception.ErrorCode.NOT_FOUND_BY_EMAIL;
import static com.sr.authboilerplate.global.exception.ErrorCode.NOT_FOUND_BY_ID;

import com.sr.authboilerplate.domain.member.entity.Member;
import com.sr.authboilerplate.domain.member.repository.MemberRepository;
import com.sr.authboilerplate.global.common.enums.MemberRole;
import com.sr.authboilerplate.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BY_ID));
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BY_EMAIL));
    }

    @Transactional
    public Long join(String email, String password) {
        validateDuplicateEmail(email);

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberRole(MemberRole.USER)
                .build();

        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(DUPLICATED_EMAIL);
        }
    }
}
