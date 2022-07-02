package com.project.mungfriend.dto.apply;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteApplyResponseDto {
    private String status;
    private String message;

    public DeleteApplyResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
