package com.project.mungfriend.service;

import com.project.mungfriend.dto.member.PhoneCheckOkRequestDto;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.util.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PhoneCheckService {

    private final MemberRepository memberRepository;

    public static String sendRandomMessage(String phoneNum) {
//        PhoneCheck message = new PhoneCheck();
        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }
        System.out.println("회원가입 문자 인증 => " + numStr);
        MessageSender.sendSMS(phoneNum, numStr);
        return numStr;
    }

    public void updatePhoneNum(PhoneCheckOkRequestDto requestDto) {

        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));

        member.setPhoneNum(requestDto.getPhoneNum());
        memberRepository.save(member);
    }
}
