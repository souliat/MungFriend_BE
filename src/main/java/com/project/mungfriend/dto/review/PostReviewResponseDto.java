package com.project.mungfriend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReviewResponseDto {
    private String status;
    private String message;

    public void ok(){
        this.status = "true";
        this.message = "리뷰 작성 성공 ! !";
    }
}
