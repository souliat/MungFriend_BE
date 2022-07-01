package com.project.mungfriend.service;

import com.project.mungfriend.dto.DeleteApplyResponseDto;
import com.project.mungfriend.dto.PostApplyRequestDto;
import com.project.mungfriend.dto.PostApplyResponseDto;
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

    // 신청 하기
    public PostApplyResponseDto registerApply(Long id, PostApplyRequestDto requestDto, String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 아이디의 회원을 찾을 수 없습니다."));

        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 하는 id의 게시글이 없습니다."));

        Apply apply = new Apply(requestDto);
        apply.setMember(member);
        apply.setPost(post);

        applyRepository.save(apply);

        return new PostApplyResponseDto("true", "신청 완료!!");
    }

    // 신청 취소
    public DeleteApplyResponseDto cancelApply(Long id, String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 아이디의 회원을 찾을 수 없습니다."));

        Apply apply = applyRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당하는 아이디의 신청을 찾을 수 없습니다."));

        if(!member.getUsername().equals(apply.getApplicant().getUsername())) {
            throw new IllegalArgumentException("본인의 신청 내역만 삭제할 수 있습니다.");
        }

        applyRepository.deleteById(id);

        return new DeleteApplyResponseDto("true", "신청이 취소 되었습니다.");
    }
}