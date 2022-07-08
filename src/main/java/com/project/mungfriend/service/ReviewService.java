package com.project.mungfriend.service;

import com.project.mungfriend.dto.review.PostReviewRequestDto;
import com.project.mungfriend.dto.review.PostReviewResponseDto;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Review;
import com.project.mungfriend.model.ReviewImageFile;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.ReviewImageFileRepository;
import com.project.mungfriend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewImageFileRepository reviewImageFileRepository;

    private final MemberRepository memberRepository;

    private final S3Uploader s3Uploader;


    @Transactional
    public PostReviewResponseDto registerReview(String username, List<MultipartFile> multipartFiles,
                                                PostReviewRequestDto requestDto) throws IOException {
        Member giver = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 회원이 존재하지 않습니다.")
        );

        Member taker = memberRepository.findByNickname(requestDto.getApplicantNickname()).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 회원이 존재하지 않습니다.")
        );

        Review review = new Review(giver, taker, requestDto);
        reviewRepository.save(review);

        for (MultipartFile multipartFile : multipartFiles) {
            // DogImageFile 엔티티에 받은 image 저장
            String imageUrl = s3Uploader.upload(multipartFile, "static");
            System.out.println("S3 업로드된 이미지 경로 : " + imageUrl);
            ReviewImageFile reviewImageFile = new ReviewImageFile(imageUrl);
            reviewImageFile.setReview(review); // 연관관계 편의 메소드
            reviewImageFileRepository.save(reviewImageFile);
        }


        PostReviewResponseDto responseDto = new PostReviewResponseDto();
        responseDto.ok();
        return responseDto;
    }

    //S3 버킷 객체 삭제
    //key 형식: "static/uuid.jpg"
    public void deleteTest(String key) {
        s3Uploader.deleteS3("static/" + key);
    }
}
