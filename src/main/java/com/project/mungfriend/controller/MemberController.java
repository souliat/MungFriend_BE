package com.project.mungfriend.controller;

import com.project.mungfriend.dto.member.MemberLoginRequestDto;
import com.project.mungfriend.dto.member.MemberSignUpRequestDto;
import com.project.mungfriend.dto.member.MemberSignUpResponseDto;
import com.project.mungfriend.dto.token.TokenDto;
import com.project.mungfriend.dto.token.TokenRequestDto;
import com.project.mungfriend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/signup")
    public ResponseEntity<MemberSignUpResponseDto> signup(@RequestBody MemberSignUpRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.signup(memberRequestDto));
    }

    @PostMapping("/member/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.login(memberRequestDto));
    }

    @PostMapping("/user/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }


    //로그인 페이지 호출
    @GetMapping("/member/login")
    public String showLoginPage(){
        return "login";
    }
}
