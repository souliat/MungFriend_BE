package com.project.mungfriend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mungfriend.exception.ErrorCode;
import com.project.mungfriend.exception.RestApiException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 필요한 권한이 없이 접근하려 할때 403
        // 바디에 넣어주기
        RestApiException restApiException = new RestApiException();
        ErrorCode errorCode = ErrorCode.FORBIDDEN_EXCEPTION;

        restApiException.setHttpStatus(errorCode.getHttpStatus());
        restApiException.setErrorMessage("403 에러가 발생하였습니다. 접근 권한이 없습니다.");

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String result = mapper.writeValueAsString(restApiException);
        response.getWriter().write(result);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
