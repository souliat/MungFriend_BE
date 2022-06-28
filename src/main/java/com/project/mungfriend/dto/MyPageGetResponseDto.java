package com.project.mungfriend.dto;

import com.project.mungfriend.model.Dog;
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

public class MyPageGetResponseDto {

    private String nickName;
    private String email;
    private String address;
    private String introduce;
    private String phoneNum;
    private List<Dog> dogList = new ArrayList<>();
    private List<Post> myPostList = new ArrayList<>();
    private List<Review> applyPostList = new ArrayList<>();
    private List<Review> reviewList = new ArrayList<>();
}
