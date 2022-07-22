package com.project.mungfriend.dto.member;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MyPagePostRequestDto {

    private String nickname;

    private String email;

    private String introduce;

    private String address;

    private String latitude;

    private String longitude;

    private Boolean isAgree;

}
