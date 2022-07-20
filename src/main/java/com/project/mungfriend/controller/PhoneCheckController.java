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


@Controller
@RequiredArgsConstructor
public class PhoneCheckController {

    private final MemberRepository memberRepository;
    private final PhoneCheckService phoneCheckService;

    @PostMapping("/phone/auth")
    @ResponseBody

    public Boolean phoneAuth(@RequestBody PhoneCheckRequestDto requestDto, HttpServletRequest request) {

        if (memberRepository.existsByPhoneNum(requestDto.getPhoneNum())) {
            return false;
        }

        String code = PhoneCheckService.sendRandomMessage(requestDto.getPhoneNum());
        request.getSession().setAttribute("rand", code);
        return true;
    }

    @PostMapping("/phone/auth/ok")
    @ResponseBody
    public Boolean phoneAuthOk(HttpServletRequest request, @RequestBody PhoneCheckOkRequestDto requestDto) {

        String rand = (String) request.getSession().getAttribute("rand");

        System.out.println(rand + " : " + requestDto.getCode());

        if (rand.equals(requestDto.getCode())) {
            request.getSession().removeAttribute("rand");
            phoneCheckService.updatePhoneNum(requestDto);
            return true;
        }
        return false;
    }
}