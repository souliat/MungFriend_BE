package com.project.mungfriend.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeletePostResponseDto {
    private String status;
    private String message;

    public void ok(){
        this.status = "true";
        this.message = "게시글 삭제 성공 ! !";
    }
}
