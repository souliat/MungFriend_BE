package com.project.mungfriend.service;

import com.project.mungfriend.dto.ApplyPostRequestDto;
import com.project.mungfriend.dto.ApplyPostResponseDto;
import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.ApplyRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ApplyRepository applyRepository;
    public ApplyPostResponseDto registerApply(Long id, ApplyPostRequestDto requestDto, String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 아이디의 회원을 찾을 수 없습니다."));

        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 하는 id의 게시글이 없습니다."));

        Apply apply = new Apply(requestDto);
        apply.setMember(member);
        apply.setPost(post);

        applyRepository.save(apply);

        return new ApplyPostResponseDto("true", "신청 완료!!");
    }
}
