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

    public Boolean phoneAuth(@RequestBody PhoneCheckRequestDto requestDto, HttpServletRequest request,
                             HttpSession session
                             ) {

        if (memberRepository.existsByPhoneNum(requestDto.getPhoneNum())) {
            throw new IllegalArgumentException("이미 등록된 휴대폰 번호입니다.");
        }

        String code = PhoneCheckService.sendRandomMessage(requestDto.getPhoneNum());

//        request.getSession().setAttribute("rand", code);
        session.setAttribute("rand", code);

        String rand = (String) session.getAttribute("rand");
        System.out.println("세션에 담은 직 후" + rand);
        return true;
    }

    @PostMapping("/phone/auth/ok")
    @ResponseBody
    public Boolean phoneAuthOk(HttpServletRequest request, @RequestBody PhoneCheckOkRequestDto requestDto,
                               HttpSession session) {

//        String rand = (String) request.getSession().getAttribute("rand");
        String rand = (String) session.getAttribute("rand");

        System.out.println(rand + " : " + requestDto.getCode());

        if (rand.equals(requestDto.getCode())) {
            request.getSession().removeAttribute("rand");
            phoneCheckService.updatePhoneNum(requestDto);
            return true;
        }
        return false;
    }
}