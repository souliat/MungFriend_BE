package com.project.mungfriend.service;

import com.project.mungfriend.dto.member.MyPageGetResponse;
import com.project.mungfriend.dto.member.MyPagePostRequestDto;
import com.project.mungfriend.dto.member.MyPagePostResponseDto;
import com.project.mungfriend.dto.token.TokenDto;
import com.project.mungfriend.enumeration.UserRole;
import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.RefreshToken;
import com.project.mungfriend.model.Review;
import com.project.mungfriend.repository.*;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.security.UserDetailsImpl;
import com.project.mungfriend.security.jwt.TokenProvider;
import com.project.mungfriend.util.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;
    private final ApplyRepository applyRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // 마이페이지에 있는 유저 정보들 보내주기 및 게시물들 중 내가 지원한 게시물이 True인 것을 찾아서 리스트로 뽑아준 것.
    public MyPageGetResponse getAllMyInfo(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 id를 찾을 수 없습니다")

        );
        MyPageGetResponse myPageGetResponseDto = new MyPageGetResponse(member);
        List<Apply> applyList = applyRepository.findAllByApplicantId(member.getId());
        for (Apply apply : applyList) {
            postRepository.findById(apply.getPost().getId()).ifPresent(
                    post -> myPageGetResponseDto.getApplyPostList().add(post));

        }

        return myPageGetResponseDto;
    }
    
    //마이페이지 수정
    public MyPagePostResponseDto updateMyPage(MyPagePostRequestDto myPageRequestDto) {
        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));

        if (memberRepository.existsByNickname(myPageRequestDto.getNickname())
                && !Objects.equals(member.getNickname(), myPageRequestDto.getNickname())) {
            String status = "false";
            String message = "중복된 닉네임이 존재합니다.";
            return MyPagePostResponseDto.of(status, message);
        }
        if (memberRepository.existsByEmail(myPageRequestDto.getEmail())
                && !Objects.equals(member.getEmail(),myPageRequestDto.getEmail())) {
            String status = "false";
            String message = "중복된 이메일이 존재합니다.";
            return MyPagePostResponseDto.of(status, message);
        }

        if (!myPageRequestDto.getIsAgree()){
            String status = "false";
            String message = "약관 동의 후 정보 수정이 가능합니다.";
            return MyPagePostResponseDto.of(status, message);
        }

        String status = "true";
        String message = "정보 수정 성공 ! !";

        member.update(myPageRequestDto);

        // 사용자 권한 체크 후 반영
        MemberValidator.updateUserRole(member);

        memberRepository.save(member);

        TokenDto tokenDto = new TokenDto();
        // 사용자 권한이 QUALIFIED_USER 라면 jwt 다시 발급하여 리턴
        if (member.getUserRole().getAuthority().equals(UserRole.Authority.QUALIFIED_USER)) {
            try {
                // 클레임에서 권한 정보 가져오기
                UserDetailsImpl userDetails = new UserDetailsImpl(member);

                // UserDetails 객체를 만들어서 Authentication 리턴 스프링 시큐리티가 제공하는 User 객체 사용
                UserDetails principal
                        = new User(member.getUsername(), "", userDetails.getAuthorities());

                Authentication authentication
                        = new UsernamePasswordAuthenticationToken(principal, "", userDetails.getAuthorities());

                // 3. 인증 정보를 기반으로 JWT 토큰 생성
                tokenDto = tokenProvider.generateTokenDto(authentication);

                // 4. RefreshToken 저장
                RefreshToken refreshToken = RefreshToken.builder()
                        .key(authentication.getName())
                        .value(tokenDto.getRefreshToken())
                        .build();

                refreshTokenRepository.save(refreshToken);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // 닉네임 수정 시 내가 작성한 후기에도 수정된 닉네임으로 반영
        List<Review> giverReviews = member.getGiverReviews();
        for (Review review : giverReviews) {
            review.setGiverNickname(member.getNickname());
            reviewRepository.save(review);
        }
        // 닉네임 수정 시 내 신청에도 수정된 닉네임으로 반영
        List<Apply> applyList = member.getApplyList();
        for (Apply apply : applyList) {
            apply.setNickname(member.getNickname());
            applyRepository.save(apply);
        }

        return MyPagePostResponseDto.of(member, status, message, tokenDto);
    }

}

