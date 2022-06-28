package com.project.mungfriend.controller;

import com.project.mungfriend.dto.ApplyPostRequestDto;
import com.project.mungfriend.dto.ApplyPostResponseDto;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.ApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping("/api/applies/{id}")
    public ApplyPostResponseDto registerApply(@PathVariable Long id, @RequestBody ApplyPostRequestDto requestDto) {
        String username = SecurityUtil.getCurrentMemberUsername();
        return applyService.registerApply(id, requestDto, username);
    }
}
