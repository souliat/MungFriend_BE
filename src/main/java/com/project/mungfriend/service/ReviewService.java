package com.project.mungfriend.service;

import com.project.mungfriend.dto.review.GetReviewDetailResponseDto;
import com.project.mungfriend.dto.review.PostReviewRequestDto;
import com.project.mungfriend.dto.review.PostReviewResponseDto;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Review;
import com.project.mungfriend.model.ReviewImageFile;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.ReviewImageFileRepository;
import com.project.mungfriend.repository.ReviewRepository;
import com.project.mungfriend.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewImageFileRepository reviewImageFileRepository;

    private final MemberRepository memberRepository;

    private final S3Uploader s3Uploader;


    // 리뷰 등록
    @Transactional
    public PostReviewResponseDto registerReview(String username, List<MultipartFile> multipartFiles,
                                                PostReviewRequestDto requestDto) throws IOException {
        Member giver = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 회원이 존재하지 않습니다.")
        );

        Member taker = memberRepository.findByNickname(requestDto.getApplicantNickname()).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 회원이 존재하지 않습니다.")
        );

        Boolean reviewByMe = reviewRepository.existsByPostIdAndGiverId(requestDto.getPostId(), giver.getId());
        if (reviewByMe) {
            throw new IllegalArgumentException("이미 후기를 작성하였으므로 또 작성할 수 없습니다.");
        }

        Review review = new Review(giver, taker, requestDto);
        reviewRepository.save(review);

        System.out.println("getName: " + multipartFiles.get(0).getName());
        System.out.println("getOriginalFilename: " + multipartFiles.get(0).getOriginalFilename());
        if(!Objects.equals(multipartFiles.get(0).getOriginalFilename(), "")){
            for (MultipartFile multipartFile : multipartFiles) {
                // DogImageFile 엔티티에 받은 image 저장
                String imageUrl = s3Uploader.upload(multipartFile, "static");
                System.out.println("S3 업로드된 이미지 경로 : " + imageUrl);
                ReviewImageFile reviewImageFile = new ReviewImageFile(imageUrl);
                reviewImageFile.setReview(review); // 연관관계 편의 메소드
                reviewImageFileRepository.save(reviewImageFile);
            }
        }

        PostReviewResponseDto responseDto = new PostReviewResponseDto();
        responseDto.ok();
        return responseDto;
    }

    //리뷰 상세 조회
    @Transactional
    public GetReviewDetailResponseDto getReviewDetail(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 ID의 리뷰가 존재하지 않습니다.")
        );

        GetReviewDetailResponseDto responseDto = new GetReviewDetailResponseDto(review);
        List<ReviewImageFile> reviewImgList = reviewImageFileRepository.findAllByReviewId(id);

        for (ReviewImageFile reviewImg : reviewImgList) {
            responseDto.getReviewImgList().add(reviewImg.getImageUrl());
        }

        return responseDto;
    }
}
