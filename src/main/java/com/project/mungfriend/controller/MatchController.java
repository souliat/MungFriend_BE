package com.project.mungfriend.controller;

import com.project.mungfriend.dto.match.MatchRequestDto;
import com.project.mungfriend.dto.match.MatchResponseDto;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class MatchController {

    private final MatchService matchService;

    // 매칭 하기
    @ResponseBody
    @PostMapping("/api/match/{id}") // 신청내역에 있는 applyId 받아와야함.
    public MatchResponseDto doneMatch(@RequestBody MatchRequestDto requestDto, @PathVariable Long id) {
        String username = SecurityUtil.getCurrentMemberUsername();
        return matchService.doneMatch(requestDto, username, id);
    }

    // 매칭 취소 하기
    @ResponseBody
    @PostMapping("/api/match/cancel")
    public MatchResponseDto cancelMatch(@RequestBody MatchRequestDto requestDto) {
        String username = SecurityUtil.getCurrentMemberUsername();
        return matchService.cancelMatch(requestDto, username);
    }
}
