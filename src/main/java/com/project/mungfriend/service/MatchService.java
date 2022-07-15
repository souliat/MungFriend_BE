package com.project.mungfriend.service;

import com.project.mungfriend.dto.match.MatchRequestDto;
import com.project.mungfriend.dto.match.MatchResponseDto;
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

    // 매칭 하기
    public MatchResponseDto doneMatch(MatchRequestDto requestDto, String username, Long id) { // id는 apply Id임.
        // 작성자(로그인한 사용자)
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 유저 ID를 찾을 수 없습니다.")
        );
        // 신청 글의 id로 해당 신청글 찾기
        Apply apply = applyRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당하는 신청글을 찾을 수 없습니다.")
        );

        // 신청글을 작성한 applicant 객체 가져오기
        Member applicant = apply.getApplicant();

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new NullPointerException("해당하는 게시글을 찾을 수 없습니다.")
        );

        if(post.getMatchedApplicantId() != null) {
            return new MatchResponseDto("false", "이미 매칭이 완료된 요청입니다.");
        }

        // 신청한 내역 post 엔티티에 업데이트
        post.setMatchedApplicantId(applicant.getId());
        post.setIsComplete(true);
        postRepository.save(post);

        // 신청자에게 문자 알림 보내기
        String phoneNum = applicant.getPhoneNum();
        String content = "[멍친구] \n" + member.getNickname() + "님과의 매칭이 완료되었습니다!";
//        MessageSender.sendSMS(phoneNum, content);

        // 신청자에게 메일 알림 보내기
        String email = applicant.getEmail();
        String title = "[멍친구] " + member.getNickname() + "님과의 매칭이 완료되었습니다!";
        String text = "[멍친구] \n" + member.getNickname() + "님과의 매칭이 완료되었습니다.";
//        MailSender.sendMail(email, title, text);

        return new MatchResponseDto("true,", "매칭이 완료 되었습니다.");
    }

    // 매칭 취소 하기
    public MatchResponseDto cancelMatch(MatchRequestDto requestDto, String username) {

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new NullPointerException("해당하는 게시글을 찾을 수 없습니다.")
        );

        Long applicantId = post.getMatchedApplicantId();
        Member applicant = memberRepository.findById(applicantId).orElseThrow(
                () -> new NullPointerException("해당하는 유저 ID를 찾을 수 없습니다.")
        );

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 유저 ID를 찾을 수 없습니다.")
        );

        // 신청한 내역 다시 원상복구
        post.setMatchedApplicantId(null);
        post.setIsComplete(false);
        postRepository.save(post);

        // 신청자에게 취소 문자 알림 보내기
        String phoneNum = applicant.getPhoneNum();
        String content = "[멍친구] \n" + member.getNickname() + "님과의 매칭이 취소되었습니다!";
        MessageSender.sendSMS(phoneNum, content);

        // 신청자에게 취소 메일 알림 보내기
        String email = applicant.getEmail();
        String title = "[멍친구] " + member.getNickname() + "님과의 매칭이 취소되었습니다!";
        String text = "[멍친구] \n" + member.getNickname() + "님과의 매칭이 취소되었습니다.";
        MailSender.sendMail(email, title, text);

        return new MatchResponseDto("true", "매칭이 취소 되었습니다.");
    }
}
