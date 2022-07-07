package com.project.mungfriend.dto.token;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String status;
    private String message;

    private String nickname;

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    public TokenDto(String status, String message){
        this.status = status;
        this.message = message;
    }
}
