package com.project.mungfriend.controller;


import com.project.mungfriend.dto.member.PhoneCheckOkRequestDto;
import com.project.mungfriend.dto.member.PhoneCheckRequestDto;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.service.PhoneCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
public class PhoneCheckController {

    private final MemberRepository memberRepository;
    private final PhoneCheckService phoneCheckService;

    @PostMapping("/phone/auth")
    @ResponseBody

    public Boolean phoneAuth(@RequestBody PhoneCheckRequestDto requestDto) {

        if (memberRepository.existsByPhoneNum(requestDto.getPhoneNum())) {
            throw new IllegalArgumentException("이미 등록된 휴대폰 번호입니다.");
        }

        // Redis에 핸드폰 인증번호 저장 (만료까지 3분)
        phoneCheckService.sendRandomMessage(requestDto.getPhoneNum());

        return true;
    }

    @PostMapping("/phone/auth/ok")
    @ResponseBody
    public Boolean phoneAuthOk(@RequestBody PhoneCheckOkRequestDto requestDto) {

        phoneCheckService.checkAndUpdatePhoneNum(requestDto);
        return true;
    }
}