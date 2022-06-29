package com.project.mungfriend.controller;


import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.MyPageGetResponse;
import com.project.mungfriend.model.MyPageIntroduceOnly;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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



    @PatchMapping("/member/mypage/introduce")
    public ResponseEntity<MyPageIntroduceOnly> partialUpdateMyPage(
            @RequestBody MyPageIntroduceOnly myPageIntroduceOnly) {

        MemberRepository.save(id);
        return ResponseEntity.ok();
    }
//
//    @PatchMapping("{Id}")
//    public String updateMember(@PathVariable Long Id, @RequestBody MyPageGetResponseDto myPageGetResponseDto){
//        Member member = myPageService.findById(Long Id);
//        MyPageService.updateMember(member, myPageGetResponseDto));
//
//        return "update success";
//    }

}