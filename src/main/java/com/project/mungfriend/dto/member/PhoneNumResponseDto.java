package com.project.mungfriend.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhoneNumResponseDto {

    private String status;
    private String message;

    public void ok() {
        this.status = "true";
        this.message = "정보 등록 성공!";
    }

}
