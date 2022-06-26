package com.project.mungfriend.dto;

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

    public static MemberSignUpResponseDto of(Member member) {
        return new MemberSignUpResponseDto(member.getUsername());
    }
}
