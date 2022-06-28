package com.project.mungfriend.service;

import com.project.mungfriend.dto.MyPageGetResponseDto;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    
    public MyPageGetResponseDto getAllMyInfo(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당하는 id를 찾을 수 없습니다")
        
        );
        MyPageGetResponseDto myPageGetResponseDto = new MyPageGetResponseDto(member);
        List<Post> applyPostList = postRepository.findByApplyByMeTrue();
        myPageGetResponseDto.getApplyPostList().addAll(applyPostList);
        return myPageGetResponseDto;
    }
}
