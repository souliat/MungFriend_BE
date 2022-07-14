package com.project.mungfriend.controller;

import com.project.mungfriend.dto.review.GetReviewDetailResponseDto;
import com.project.mungfriend.dto.review.PostReviewRequestDto;
import com.project.mungfriend.dto.review.PostReviewResponseDto;
import com.project.mungfriend.security.SecurityUtil;
import com.project.mungfriend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 등록 (다중 이미지 업로드)
    @PostMapping("/api/reviews")
    public PostReviewResponseDto registerReview(@RequestPart(value="image", required = false) List<MultipartFile> multipartFiles,
                                                @RequestPart(value="infos") PostReviewRequestDto requestDto) throws IOException {
        String username = SecurityUtil.getCurrentMemberUsername();
        return reviewService.registerReview(username, multipartFiles, requestDto);
    }

    //리뷰 상세 조회
    @GetMapping("/api/reviews/{id}")
    public GetReviewDetailResponseDto getReviewDetail(@PathVariable Long id){
        return reviewService.getReviewDetail(id);
    }
}
