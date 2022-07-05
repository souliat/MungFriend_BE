package com.project.mungfriend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDto {
    private String status;
    private String message;

    public void MatchResponseDto(String status, String message){
        this.status = "true";
        this.message = "매칭이 완료되었습니다.";
    }
}
