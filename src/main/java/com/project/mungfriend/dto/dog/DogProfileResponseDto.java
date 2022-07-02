package com.project.mungfriend.dto.dog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DogProfileResponseDto {
    private String status;
    private String message;

    public DogProfileResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
