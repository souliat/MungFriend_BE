package com.project.mungfriend.dto.member;

import com.project.mungfriend.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetMyInfoResponseDto {
    private Long id;
    private String nickname;
    private String username;
    private String userRole;
    private String email;

    public GetMyInfoResponseDto(Member member){
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
        this.userRole = member.getUserRole().toString();
        this.email = member.getEmail();
    }
}
