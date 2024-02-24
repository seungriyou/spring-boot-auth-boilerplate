package com.sr.authboilerplate.domain.auth.repository;

import com.sr.authboilerplate.domain.auth.entity.Auth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByRefreshToken(String refreshToken);

    Optional<Auth> findByMemberId(Long memberId);

}
