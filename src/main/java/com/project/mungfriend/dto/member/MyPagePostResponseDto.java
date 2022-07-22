package com.project.mungfriend.dto.member;

import com.project.mungfriend.dto.token.TokenDto;
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

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    public static MyPagePostResponseDto of(Member member, String status, String message, TokenDto tokenDto) {
        return new MyPagePostResponseDto(member.getUsername(), status, message,
                tokenDto.getGrantType(), tokenDto.getAccessToken(),
                tokenDto.getRefreshToken(), tokenDto.getAccessTokenExpiresIn());
    }

    public static MyPagePostResponseDto of(String status, String message) {
        return new MyPagePostResponseDto("error",status, message,
                null, null, null, null);
    }
}
