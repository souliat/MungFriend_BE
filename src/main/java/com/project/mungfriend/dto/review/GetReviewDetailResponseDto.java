package com.project.mungfriend.dto.review;


import com.project.mungfriend.model.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetReviewDetailResponseDto {
    private Long id;
    private String giverNickname;
    private Long postId;
    private String comment;
    private String giverDogProfileImgUrl;
    private LocalDateTime createdAt;
    List<String> reviewImgList;

    public GetReviewDetailResponseDto(Review review){
        this.id = review.getId();
        this.giverNickname = review.getGiverNickname();
        this.comment = review.getComment();
        this.giverDogProfileImgUrl = review.getGiverDogProfileImgUrl();
        this.createdAt = review.getCreatedAt();
    }
}
