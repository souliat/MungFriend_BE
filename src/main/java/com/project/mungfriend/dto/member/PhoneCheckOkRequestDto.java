package com.project.mungfriend.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class PhoneCheckOkRequestDto {

    private String phoneNum;
    private String code;
}
