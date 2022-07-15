package com.project.mungfriend.dto.member;

import com.project.mungfriend.model.Dog;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.model.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class MyPageGetResponse {

    private String nickname;
    private String email;
    private String address;
    private String latitude;
    private String longitude;
    private String introduce;
    private String phoneNum;
    private Boolean isAgree;
    private List<Dog> dogList = new ArrayList<>();
    private List<Post> myPostList = new ArrayList<>();
    private List<Post> applyPostList = new ArrayList<>();
    private List<Review> reviewList = new ArrayList<>();

//    public MyPageGetResponseDto(){
//
//    }
    public MyPageGetResponse(Member member){
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.latitude = member.getLatitude();
        this.longitude = member.getLongitude();
        this.introduce = member.getIntroduce();
        this.phoneNum = member.getPhoneNum();
        this.isAgree = member.getIsAgree();
        this.dogList = member.getDogList();
        this.myPostList = member.getMyPostList();
//      통합 후 getter이름 변경
        this.reviewList = member.getTakerReviews();

    }
}
