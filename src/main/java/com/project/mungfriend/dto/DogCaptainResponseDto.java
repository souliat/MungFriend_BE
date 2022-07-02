package com.project.mungfriend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DogCaptainResponseDto {
    private String status;
    private String message;

    public DogCaptainResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

