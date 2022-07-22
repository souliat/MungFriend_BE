package com.project.mungfriend.service;

import com.project.mungfriend.dto.member.PhoneCheckOkRequestDto;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PhoneCheckNumberRepository;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.util.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PhoneCheckService {

    private final MemberRepository memberRepository;
    private final PhoneCheckNumberRepository phoneCheckNumberRepository;

    public void sendRandomMessage(String phoneNum) {
//        PhoneCheck message = new PhoneCheck();
        Random rand = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            code += ran;
        }
        System.out.println("회원가입 문자 인증 => " + code);
        MessageSender.sendSMS(phoneNum, code);

        //Redis 저장소 사용부분
        phoneCheckNumberRepository.savePhoneCheckNum(phoneNum, code);

    }

    public void checkAndUpdatePhoneNum(PhoneCheckOkRequestDto requestDto) {
        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));

        String phoneCheckNum = phoneCheckNumberRepository.getPhoneCheckNum(requestDto.getPhoneNum());
        System.out.println("phoneCheckNum = " + phoneCheckNum);

        if( phoneCheckNum.equals(requestDto.getCode())){
            member.setPhoneNum(requestDto.getPhoneNum());
            memberRepository.save(member);
            phoneCheckNumberRepository.removePhoneCheckNum(requestDto.getPhoneNum());
        }else{
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }
    }
}
