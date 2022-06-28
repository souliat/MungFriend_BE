package com.project.mungfriend.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class MyPageController {


    @GetMapping("/member/mypage/{id}")
    @ResponseBody
    public String currentUserName(Authentication authentication)
    {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
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