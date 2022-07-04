package com.project.mungfriend.controller;


import com.project.mungfriend.dto.member.*;
import com.project.mungfriend.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


//Responsebody+Controller 느낌
@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;



    @GetMapping("/member/mypage/{id}")
    public MyPageGetResponse getAllMyInfo(@PathVariable Long id) {
//        String username = SecurityUtil.getCurrentMemberUsername();
        return myPageService.getAllMyInfo(id);
    }


// Json형식으로 들어온 자기소개 정보를 Java로 바꿔서 연결시켜 준 후에
// myPageService에 만들어 둔 메소드 updateIntroduce(updatePhoneNum)의 결과를 IntroduceRequestDto(updatePhoneNum)쪽으로 보낸다
    @PatchMapping("/mypage/introduce")
    public IntroduceResponseDto updateIntroduce(@RequestBody IntroduceRequestDto requestDto) {

        return myPageService.updateIntroduce(requestDto);
    }




}