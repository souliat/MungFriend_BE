package com.project.mungfriend.service;

import com.project.mungfriend.dto.RegisterPostRequestDto;
import com.project.mungfriend.dto.RegisterPostResponseDto;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    //게시글 등록
    @Transactional
    public RegisterPostResponseDto registerPost(String username,
                                                RegisterPostRequestDto requestDto) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("게시글 작성은 로그인 후 가능합니다.")
        );
        Post post = new Post(requestDto);
        post.setMember(member);
        postRepository.save(post);

        RegisterPostResponseDto registerPostResponseDto = new RegisterPostResponseDto();
        registerPostResponseDto.ok();
        return registerPostResponseDto;
    }
}
