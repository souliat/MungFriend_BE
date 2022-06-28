package com.project.mungfriend.controller;


import com.project.mungfriend.dto.MyPageGetResponseDto;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


//Responsebody+Controller 느낌
@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/member/mypage/{id}")
    public MyPageGetResponseDto getAllMyInfo(@PathVariable Long id) {
//        String username = SecurityUtil.getCurrentMemberUsername();
        return myPageService.getAllMyInfo(id);
    }

//    @GetMapping("/member/mypage/{id}")
//    public Member show(@PathVariable Long id) {
//        Member member = memberRepository.findById(id);
//        return Member.body(MemberRequestDto.from(member));
//    }

}


//    @GetMapping("/api/detail/{id}")
//    public List<House> getAllHouse(@PathVariable Long id){
//        List<House> house= houseRepository.findAllById(id);
//        return house;
//    }