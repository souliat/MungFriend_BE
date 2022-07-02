package com.project.mungfriend.controller;

import com.project.mungfriend.dto.apply.DeleteApplyResponseDto;
import com.project.mungfriend.dto.apply.PostApplyRequestDto;
import com.project.mungfriend.dto.apply.PostApplyResponseDto;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.ApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    //신청 하기
    @PostMapping("/api/applies/{id}") // 여기서 아이디는 postId.
    public PostApplyResponseDto registerApply(@PathVariable Long id, @RequestBody PostApplyRequestDto requestDto) {
        String username = SecurityUtil.getCurrentMemberUsername();
        return applyService.registerApply(id, requestDto, username);
    }

    // 신청 취소
    @DeleteMapping("/api/applies/{id}") // 여기서 아이디는 applyId.
    public DeleteApplyResponseDto cancelApply(@PathVariable Long id) {
        String username = SecurityUtil.getCurrentMemberUsername();
        return applyService.cancelApply(id, username);
    }
}
