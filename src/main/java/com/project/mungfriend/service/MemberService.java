package com.project.mungfriend.service;

import com.project.mungfriend.dto.*;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.RefreshToken;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.RefreshTokenRepository;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    public MemberSignUpResponseDto signup(MemberSignUpRequestDto memberRequestDto) {
        if (memberRepository.existsByUsername(memberRequestDto.getUsername())) {
            //throw new RuntimeException("중복된 username입니다");
            String status = "false";
            String message = "중복된 아이디가 존재합니다.";
            return MemberSignUpResponseDto.of(status, message);
        }

        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            //throw new RuntimeException("중복된 username입니다");
            String status = "false";
            String message = "중복된 이메일이 존재합니다.";
            return MemberSignUpResponseDto.of(status, message);
        }

        if (memberRepository.existsByNickname(memberRequestDto.getNickname())) {
            //throw new RuntimeException("중복된 username입니다");
            String status = "false";
            String message = "중복된 닉네임이 존재합니다.";
            return MemberSignUpResponseDto.of(status, message);
        }

        String status = "true";
        String message = "회원가입 성공 ! !";

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberSignUpResponseDto.of(memberRepository.save(member), status, message);
    }

    public TokenDto login(MemberLoginRequestDto memberRequestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 UserDetailsServiceImpl 에서 만들었던 loadUserByUsername 메서드가 실행됨

        try {
            authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        }catch (Exception e){
            e.printStackTrace();
            String status = "false";
            String message = "잘못된 아이디 혹은 패스워드입니다.";
            return new TokenDto(status, message);
        }

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        String status = "true";
        String message = "로그인 성공! !";
        tokenDto.setStatus(status);
        tokenDto.setMessage(message);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}
