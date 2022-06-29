package com.project.mungfriend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class MyPageGetResponse {

    private String nickName;
    private String email;
    private String address;
    private String introduce;
    private String phoneNum;
    private List<Dog> dogList = new ArrayList<>();
    private List<Post> myPostList = new ArrayList<>();
    private List<Post> applyPostList = new ArrayList<>();
    private List<Review> reviewList = new ArrayList<>();

//    public MyPageGetResponseDto(){
//
//    }
    public MyPageGetResponse(Member member){
        this.nickName = member.getNickname();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.introduce = member.getIntroduce();
        this.phoneNum = member.getPhoneNum();
        this.dogList = member.getDogList();
        this.myPostList = member.getMyPostList();
//      통합 후 getter이름 변경
        this.reviewList = member.getTakerReviews();

    }
}
