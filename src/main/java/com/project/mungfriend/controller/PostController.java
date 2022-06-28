package com.project.mungfriend.controller;

import com.project.mungfriend.dto.*;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //게시글 등록
    @PostMapping("/api/posts")
    public RegisterPostResponseDto registerPost(@RequestBody RegisterPostRequestDto requestDto){
        String username = SecurityUtil.getCurrentMemberUsername();
        return postService.registerPost(username, requestDto);
    }

    //게시글 전체 조회
    @GetMapping("/api/posts")
    public List<GetPostResponseDto> getAllPosts(){
        return postService.getAllPosts();
    }

    //게시글 상세 조회
    @GetMapping("/api/posts/{id}")
    public GetPostDetailResponseDto getPostDetail(@PathVariable Long id){
        String username = SecurityUtil.getCurrentMemberUsername();
        return postService.getPostDetail(id, username);
    }

    //게시글 수정
    @PutMapping("/api/posts/{id}")
    public PutPostResponseDto updatePost(@PathVariable Long id, @RequestBody PutPostRequestDto requestDto){
        String username = SecurityUtil.getCurrentMemberUsername();
        return postService.updatePost(id, requestDto, username);
    }

    //게시글 삭제
    @DeleteMapping("/api/posts/{id}")
    public DeletePostResponseDto deletePost(@PathVariable Long id){
        String username = SecurityUtil.getCurrentMemberUsername();
        return postService.deletePost(id, username);
    }
}
