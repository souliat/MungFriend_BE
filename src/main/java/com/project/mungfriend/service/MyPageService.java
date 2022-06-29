package com.project.mungfriend.service;

import com.project.mungfriend.model.MyPageGetResponse;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 마이페이지에 있는 유저 정보들 보내주기 및 게시물들 중 내가 지원한 게시물이 True인 것을 찾아서 리스트로 뽑아준 것.
    public MyPageGetResponse getAllMyInfo(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당하는 id를 찾을 수 없습니다")
        
        );
        MyPageGetResponse myPageGetResponseDto = new MyPageGetResponse(member);
        List<Post> applyPostList = postRepository.findByApplyByMeTrue();
        myPageGetResponseDto.getApplyPostList().addAll(applyPostList);
        return myPageGetResponseDto;
    }

//    @PatchMapping("/person")
//    public Member patchmember(@RequestParam("column") String column, @RequestParam("tobe") String tobe, @RequestParam("id") long id) {
//        Person modifiedPerson = personService.patchPersonCompany(column,tobe , id);
//
//        return modifiedPerson;


//    public static Member updateMember(Member member, MyPageGetResponseDto MyPageGetResponseDto){
//        member.updateMember(MyPageGetResponseDto);
//        return member;
//    }
}
