package com.project.mungfriend.controller;

import com.project.mungfriend.dto.RegisterPostRequestDto;
import com.project.mungfriend.dto.RegisterPostResponseDto;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/api/posts")
    public RegisterPostResponseDto registerPost(@RequestBody RegisterPostRequestDto requestDto){
        String username = SecurityUtil.getCurrentMemberUsername();
        return postService.registerPost(username, requestDto);
    }
}
