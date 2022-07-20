package com.project.mungfriend.service;

import com.project.mungfriend.dto.apply.DeleteApplyResponseDto;
import com.project.mungfriend.dto.apply.PostApplyRequestDto;
import com.project.mungfriend.dto.apply.PostApplyResponseDto;
import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.ApplyRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

        //모집 종료된 게시글에는 신청하지 못하게 막기
        if(post.getIsComplete()){
            throw new IllegalArgumentException("모집 종료된 게시글에는 신청할 수 없습니다.");
        }

        //나의 게시글에 신청하지 못하게 하기
        if(Objects.equals(post.getMember().getId(), member.getId())){
            throw new IllegalArgumentException("나의 게시글에는 신청할 수 없습니다.");
        }

        // 이미 신청했다면 또 신청되지 않게 하기
        Boolean applyByMe = applyRepository.existsByApplicantIdAndPostId(member.getId(), post.getId());
        if (applyByMe) {
            throw new IllegalArgumentException("이미 신청하였으므로 또 신청할 수 없습니다.");
        }

        Apply apply = new Apply(requestDto);
        apply.setMember(member);
        apply.setPost(post);

        applyRepository.save(apply);

        return new PostApplyResponseDto("true", "신청 완료!!");
    }

    // 신청 취소
    public DeleteApplyResponseDto cancelApply(Long postId, String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 아이디의 회원을 찾을 수 없습니다."));

        Apply apply = applyRepository.findByApplicantIdAndPostId(member.getId(), postId).orElseThrow(
                () -> new NullPointerException("해당하는 아이디의 신청을 찾을 수 없습니다.")
        );

        if(!member.getUsername().equals(apply.getApplicant().getUsername())) {
            throw new IllegalArgumentException("본인의 신청 내역만 삭제할 수 있습니다.");
        }

        applyRepository.delete(apply);

        return new DeleteApplyResponseDto("true", "신청이 취소 되었습니다.");
    }
}
