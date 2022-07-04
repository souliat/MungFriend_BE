package com.project.mungfriend.controller;

import com.project.mungfriend.dto.MatchRequestDto;
import com.project.mungfriend.dto.MatchResponseDto;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class MatchController {

    private final MatchService matchService;

    @ResponseBody
    @PostMapping("/api/match/{id}") // 신청내역에 있는 applyId 받아와야함.
    public MatchResponseDto doneMatch(@RequestBody MatchRequestDto requestDto, @PathVariable Long id) {
        String username = SecurityUtil.getCurrentMemberUsername();
        return matchService.doneMatch(requestDto, username, id);
    }
}
