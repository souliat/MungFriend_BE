package com.project.mungfriend.dto;

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
    private Long applicantId;
    private String comment;

}
