package com.project.mungfriend.dto.member;

import com.project.mungfriend.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class MyPagePostResponseDto {

    private String username;
    private String status;
    private String message;

    public static MyPagePostResponseDto of(Member member, String status, String message) {
        return new MyPagePostResponseDto(member.getUsername(), status, message);
    }

    public static MyPagePostResponseDto of(String status, String message) {
        return new MyPagePostResponseDto("error",status, message);
    }
}
