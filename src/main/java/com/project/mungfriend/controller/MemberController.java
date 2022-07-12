package com.project.mungfriend.controller;

import com.project.mungfriend.dto.member.*;
import com.project.mungfriend.dto.token.TokenDto;
import com.project.mungfriend.dto.token.TokenRequestDto;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/member/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }

    // 로그인한 사용자의 정보 리턴 (id, nickname, username)
    @PostMapping("/myinfo")
    @ResponseBody
    public GetMyInfoResponseDto getMyInfo(){
        String username = SecurityUtil.getCurrentMemberUsername();
        return memberService.getMyInfo(username);
    }

    // 상세페이지에서 닉네임 클릭 시 해당 회원의 정보 리턴
    @GetMapping("/userinfo")
    @ResponseBody
    public GetUserInfoResponseDto getUserInfo(@RequestBody GetUserInfoRequestDto requestDto){
        return memberService.getUserInfo(requestDto);
    }


    //로그인 페이지 호출
    @GetMapping("/member/login")
    public String showLoginPage(){
        return "login";
    }
}
