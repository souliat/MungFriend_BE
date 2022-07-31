package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mungfriend.dto.member.MyPagePostRequestDto;
import com.project.mungfriend.enumeration.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String address="";

    @Column(nullable = false)
    private String latitude="";

    @Column(nullable = false)
    private String longitude="";

    @Column(nullable = false)
    private String phoneNum= "";

    @Column(nullable = false)
    private String introduce = "";

    @Column
    private String dogProfileImgUrl = "https://s3.ap-northeast-2.amazonaws.com/code10.shop/assets/images/Yebin/profileImg.png";


    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole = UserRole.USER;

    @Column(nullable = false)
    private Boolean isAgree = false;

    @Column
    private String provider;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Post> myPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Dog> dogList = new ArrayList<>();

    @OneToMany(mappedBy = "taker")
    @JsonIgnore
    private List<Review> takerReviews = new ArrayList<>();

    @OneToMany(mappedBy = "giver")
    @JsonIgnore
    private List<Review> giverReviews = new ArrayList<>();

    @OneToMany(mappedBy = "applicant")
    @JsonIgnore
    private List<Apply> applyList = new ArrayList<>();

    @Builder
    public Member(String username, String password, UserRole userRole, String nickname,
                  String email, String address, String latitude, String longitude, boolean isAgree) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.nickname = nickname;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isAgree = isAgree;

    }

    public Member(String username, String email, String password, String nickname, UserRole userRole, String provider) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.nickname = nickname;
        this.address = "";
        this.email = email;
        this.provider = provider;

    }

    // 회원 정보 업데이트
    public void update(MyPagePostRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.email = requestDto.getEmail();
        this.address = requestDto.getAddress();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
        this.introduce = requestDto.getIntroduce();
        this.isAgree = requestDto.getIsAgree();
    }
}
