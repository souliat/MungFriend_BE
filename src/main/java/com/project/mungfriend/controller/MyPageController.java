package com.project.mungfriend.controller;


import com.project.mungfriend.dto.member.MyPageGetResponse;
import com.project.mungfriend.dto.member.MyPagePostRequestDto;
import com.project.mungfriend.dto.member.MyPagePostResponseDto;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


//Responsebody+Controller 느낌
@RestController
@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;


    //마이페이지 조회
    @GetMapping("/mypage")
    public MyPageGetResponse getAllMyInfo() {
        String username = SecurityUtil.getCurrentMemberUsername();
        return myPageService.getAllMyInfo(username);
    }

    //마이페이지 수정
    @PostMapping("/mypage")
    public MyPagePostResponseDto updateMyPage(@RequestBody MyPagePostRequestDto mypageRequestDto) {
        return myPageService.updateMyPage(mypageRequestDto);
    }
}
