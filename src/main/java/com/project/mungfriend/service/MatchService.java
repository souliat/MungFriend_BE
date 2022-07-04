package com.project.mungfriend.service;

import com.project.mungfriend.dto.MatchRequestDto;
import com.project.mungfriend.dto.MatchResponseDto;
import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.ApplyRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
import com.project.mungfriend.util.MailSender;
import com.project.mungfriend.util.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MatchService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ApplyRepository applyRepository;
    public MatchResponseDto doneMatch(MatchRequestDto requestDto, String username, Long id) { // id는 apply Id임.
        // 작성자(로그인한 사용자)
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 유저 ID를 찾을 수 없습니다.")
        );
        // 신청 글의 id로 해당 신청글 찾기
        Apply apply = applyRepository.findById(id).orElseThrow(
                () -> new NullPointerException()
        );

        // 신청글을 작성한 applicant 객체 가져오기
        Member applicant = apply.getApplicant();

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new NullPointerException("해당하는 게시글을 찾을 수 없습니다.")
        );

        post.setMatchedApplicantId(id); // post 엔티티에 매칭된 신청자의 고유값 저장.
        post.setIsComplete(true);  // 게시글의 상태는 모집 종료로 변경
        postRepository.save(post);

        // 신청자에게 문자 알림 보내기
        String phoneNum = applicant.getPhoneNum();
        String content = "[멍친구] \n" + member.getNickname() + "님과의 매칭이 완료되었습니다!";
        MessageSender.sendSMS(phoneNum, content);

        // 신청자에게 메일 알림 보내기
        String email = applicant.getEmail();
        String title = "[멍친구] " + member.getNickname() + "님과의 매칭이 완료되었습니다!";
        String text = "[멍친구] \n" + member.getNickname() + "님과의 매칭이 완료되었습니다.";
        MailSender.sendMail(email, title, text);

        return new MatchResponseDto("true,", "매칭이 완료되었습니다.");
    }
}
