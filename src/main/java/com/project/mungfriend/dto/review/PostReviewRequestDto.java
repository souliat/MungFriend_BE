package com.project.mungfriend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReviewRequestDto {
    private Long postId;
    private String applicantNickname;
    private String comment;

}
