package com.project.mungfriend.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterPostResponseDto {
    private String status;
    private String message;

    public void ok(){
        this.status = "true";
        this.message = "게시글 작성 성공 ! !";
    }
}
