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
public class MemberSignUpResponseDto {

    private String username;
    private String status;
    private String message;

    public static MemberSignUpResponseDto of(Member member, String status, String message) {
        return new MemberSignUpResponseDto(member.getUsername(), status, message);
    }

    public static MemberSignUpResponseDto of(String status, String message) {
        return new MemberSignUpResponseDto("error",status, message);
    }
}
