package com.project.mungfriend.dto.member;

import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetUserInfoResponseDto {
    private String nickname;
    private String introduce;
    private List<Review> reviewList = new ArrayList<>();

    public GetUserInfoResponseDto(Member member){
        this.nickname = member.getNickname();
        this.introduce = member.getIntroduce();
    }
}
