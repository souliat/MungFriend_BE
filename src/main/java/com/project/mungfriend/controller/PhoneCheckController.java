package com.project.mungfriend.controller;

import com.project.mungfriend.dto.member.PhoneCheckOkRequestDto;
import com.project.mungfriend.dto.member.PhoneCheckRequestDto;
import com.project.mungfriend.service.MemberService;
import com.project.mungfriend.service.PhoneCheckService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller

public class PhoneCheckController {

    @PostMapping("/phoneAuth")
    @ResponseBody

    public Boolean phoneAuth(@RequestBody PhoneCheckRequestDto requestDto, HttpServletRequest request) {

//        try { // 이미 가입된 전화번호가 있으면
//            if(PhoneCheckService.memberTelCount(tel) > 0)
//                return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String code = PhoneCheckService.sendRandomMessage(requestDto.getPhoneNum());
        request.getSession().setAttribute("rand", code);

        return true;
    }

    @PostMapping("/phoneAuthOk")
    @ResponseBody
    public Boolean phoneAuthOk(HttpServletRequest request, @RequestBody PhoneCheckOkRequestDto requestDto) {
        String rand = (String) request.getSession().getAttribute("rand");

        System.out.println(rand + " : " + requestDto.getCode());

        if (rand.equals(requestDto.getCode())) {
            request.getSession().removeAttribute("rand");
            return true;
        }

        return false;
    }
}
