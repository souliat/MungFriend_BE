package com.project.mungfriend.service;

import com.project.mungfriend.dto.member.*;
import com.project.mungfriend.dto.token.TokenDto;
import com.project.mungfriend.dto.token.TokenRequestDto;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.RefreshToken;
import com.project.mungfriend.model.Review;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.RefreshTokenRepository;
import com.project.mungfriend.security.jwt.TokenProvider;
import com.project.mungfriend.util.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
            String status = "false";
            String message = "중복된 아이디가 존재합니다.";
            return MemberSignUpResponseDto.of(status, message);
        }

        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            String status = "false";
            String message = "중복된 이메일이 존재합니다.";
            return MemberSignUpResponseDto.of(status, message);
        }

        if (memberRepository.existsByNickname(memberRequestDto.getNickname())) {
            String status = "false";
            String message = "중복된 닉네임이 존재합니다.";
            return MemberSignUpResponseDto.of(status, message);
        }

        String status = "true";
        String message = "회원가입 성공 ! !";

        Member member = memberRequestDto.toMember(passwordEncoder);

        //회원 가입 축하 메일 보내기 (일단 주석 처리 해둠) ssl
        // No appropriate protocol (protocol is disabled or cipher suites are inappropriate) 에러메세지
        MailSender.sendMail(member.getEmail(), "회원가입을 축하드립니다!", "환영합니다. 우리의 멍친구!");

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

        // tokenDto 값 set
        String status = "true";
        String message = "로그인 성공! !";
        tokenDto.setStatus(status);
        tokenDto.setMessage(message);

        Member user = memberRepository.findByUsername(authentication.getName()).orElse(null);
        assert user != null;
        tokenDto.setMemberId(user.getId());
        tokenDto.setNickname(user.getNickname());

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

    // 로그인한 사용자의 정보 리턴
    public GetMyInfoResponseDto getMyInfo(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("일치하는 회원 정보가 없습니다.")
        );
        return new GetMyInfoResponseDto(member);
    }

    // 로그인한 사용자 객체 리턴
    public Member getMemberObject(String username){
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("일치하는 회원 정보가 없습니다.")
        );
    }

    // 상세페이지에서 닉네임 클릭 시 해당 회원의 정보 리턴
    public GetUserInfoResponseDto getUserInfo(GetUserInfoRequestDto requestDto) {
        Member member = memberRepository.findByNickname(requestDto.getNickname()).orElseThrow(
                () -> new IllegalArgumentException("일치하는 회원 정보가 없습니다.")
        );

        List<Review> takerReviews = member.getTakerReviews();

        GetUserInfoResponseDto responseDto = new GetUserInfoResponseDto(member);
        List<Review> reviewList = responseDto.getReviewList();

        // 역순으로 최신 리뷰 3개 !
        Collections.reverse(takerReviews);

        for (Review review : takerReviews) {
            reviewList.add(review);
            if(reviewList.size() == 3){
                break;
            }
        }

        return responseDto;

    }
}
