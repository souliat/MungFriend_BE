package com.project.mungfriend.service;

import com.project.mungfriend.dto.member.*;
import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.ApplyRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
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

    private final ApplyRepository applyRepository;

    // 마이페이지에 있는 유저 정보들 보내주기 및 게시물들 중 내가 지원한 게시물이 True인 것을 찾아서 리스트로 뽑아준 것.
    public MyPageGetResponse getAllMyInfo(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당하는 id를 찾을 수 없습니다")

        );
        MyPageGetResponse myPageGetResponseDto = new MyPageGetResponse(member);
        List<Apply> applyList = applyRepository.findAllByApplicantId(id);
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

        member.setNickname(mypageRequestDto.getNickname());
        member.setEmail(mypageRequestDto.getEmail());
        member.setAddress(mypageRequestDto.getAddress());
        member.setLatitude(mypageRequestDto.getLatitude());
        member.setLongitude(mypageRequestDto.getLongitude());
        member.setIntroduce(mypageRequestDto.getIntroduce());
        member.setPhoneNum(mypageRequestDto.getPhoneNum());
        member.setIsAgree(mypageRequestDto.getIsAgree());

        return MyPagePostResponseDto.of(memberRepository.save(member), status, message);
    }

}

