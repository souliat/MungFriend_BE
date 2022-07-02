package com.project.mungfriend.security.oauth.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mungfriend.dto.token.TokenDto;
import com.project.mungfriend.model.RefreshToken;
import com.project.mungfriend.repository.RefreshTokenRepository;
import com.project.mungfriend.security.UserDetailsImpl;
import com.project.mungfriend.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        String targetUrl;

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 최초 로그인이라면 회원가입 처리를 한다.

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

//        tokenDto.setUsername(userDetails.getUsername());
//        tokenDto.setNickname(userDetails.getNickname());

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        //바디에 넣어주기
        String result = mapper.writeValueAsString(tokenDto);
        response.getWriter().write(result);

        // 헤더에 넣어주기
        response.addHeader("Authorization", tokenDto.getAccessToken());
        response.addHeader("username", userDetails.getUsername());
        response.addHeader("nickname", userDetails.getNickname());


        //5. 토큰 발급
        targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth") // 프론트 서버 반영 (로컬 / 서버)
                .encode()
                .queryParam("Authorization", tokenDto.getAccessToken())
                .queryParam("username", userDetails.getUsername())
                .queryParam("nickname", userDetails.getNickname())
//                .queryParam("profile", userDetails.getProfileImg())
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
