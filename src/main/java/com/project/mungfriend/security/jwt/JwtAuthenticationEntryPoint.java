package com.project.mungfriend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mungfriend.exception.ErrorCode;
import com.project.mungfriend.exception.RestApiException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        // 바디에 넣어주기
        RestApiException restApiException = new RestApiException();
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_EXCEPTION;

        restApiException.setHttpStatus(errorCode.getHttpStatus());
        restApiException.setErrorMessage("401 에러가 발생하였습니다. 유효한 자격이 아닙니다.");

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String result = mapper.writeValueAsString(restApiException);
        response.getWriter().write(result);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // sendError 시 위에서 바디에 담아준 내용이 가지 않는다..
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
