package com.project.mungfriend.service;

import com.project.mungfriend.dto.member.*;
import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Review;
import com.project.mungfriend.repository.ApplyRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
import com.project.mungfriend.repository.ReviewRepository;
import com.project.mungfriend.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
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
    public MyPagePostResponseDto updateMyPage(MyPagePostRequestDto mypageRequestDto) {
        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));

        if (memberRepository.existsByNickname(mypageRequestDto.getNickname()) && !Objects.equals(member.getNickname(), mypageRequestDto.getNickname())) {
            String status = "false";
            String message = "중복된 닉네임이 존재합니다.";
            return MyPagePostResponseDto.of(status, message);
        }
        if (memberRepository.existsByEmail(mypageRequestDto.getEmail()) && !Objects.equals(member.getEmail(),mypageRequestDto.getEmail())) {
            String status = "false";
            String message = "중복된 이메일이 존재합니다.";
            return MyPagePostResponseDto.of(status, message);
        }
        if (memberRepository.existsByPhoneNum(mypageRequestDto.getPhoneNum()) && !Objects.equals(member.getPhoneNum(),mypageRequestDto.getPhoneNum())) {
            String status = "false";
            String message = "중복된 번호가 존재합니다.";
            return MyPagePostResponseDto.of(status, message);
        }

        String status = "true";
        String message = "정보 수정 성공 ! !";

        member.update(mypageRequestDto);
        memberRepository.save(member);

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

        return MyPagePostResponseDto.of(member, status, message);
    }

}

