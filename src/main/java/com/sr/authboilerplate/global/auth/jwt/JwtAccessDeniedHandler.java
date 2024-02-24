package com.sr.authboilerplate.global.auth.jwt;

import static com.sr.authboilerplate.global.exception.ExceptionLoggingUtil.logWarn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.authboilerplate.global.common.dto.ErrorResponse;
import com.sr.authboilerplate.global.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.HANDLE_ACCESS_DENIED_EXCEPTION;

        logWarn(accessDeniedException);

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter()
                .write(objectMapper.writeValueAsString((
                        ErrorResponse.from(errorCode)
                )));
    }

}
