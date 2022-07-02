package com.project.mungfriend.dto.apply;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostApplyResponseDto {

    private String status;
    private String message;

    public PostApplyResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }

}
