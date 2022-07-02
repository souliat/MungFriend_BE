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

    public IntroduceResponseDto updateIntroduce(IntroduceRequestDto requestDto) {
        //SecurityUtil에 등록된 방식으로 현재의 유저네임을 가져온 후에 memberRepository에 있는 유저네임에 넣어서 해당 유저네일을 찾는다.
        //없다면 에러 메세지를 가져오고 찾았다면 그 유저네임을 가진 member 클래스를 가져온다 (Member member = 객체 인스턴스)
        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));

        // requestDto를 통해 받아온 Introduce를 가져온다 (get) 그 후 member의 Introduce에 설정한다.(set)
        member.setIntroduce(requestDto.getIntroduce());
        // 위에서 설정한 것을 맴버 DB에 저장한다.(memberRepository를 통해서 member의 db와 연결하고 save를 통해 저장)
        memberRepository.save(member);
        //위 과정을 통해 새로운 IntroduceResponseDto를 IntroduceResponseDto responseDto에 넣어준다 (기존 객체에?)
        IntroduceResponseDto responseDto = new IntroduceResponseDto();

        responseDto.ok();
        return responseDto;
    }

    public PhoneNumResponseDto updatePhoneNum(PhoneNumRequestDto requestDto) {

        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));


        member.setPhoneNum(requestDto.getPhoneNum());
        memberRepository.save(member);

        PhoneNumResponseDto responseDto = new PhoneNumResponseDto();
        responseDto.ok();
        return responseDto;
    }

}
