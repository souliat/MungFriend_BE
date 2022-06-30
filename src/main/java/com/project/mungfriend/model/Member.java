package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole = UserRole.USER;

    @Column(nullable = false)
    private Boolean isAgree = false;

    @Column
    private String provider;

    @OneToMany(mappedBy = "member")
    private List<Post> myPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Dog> dogList = new ArrayList<>();

    @OneToMany(mappedBy = "taker")
    private List<Review> takerReviews = new ArrayList<>();

    @OneToMany(mappedBy = "giver")
    private List<Review> giverReviews = new ArrayList<>();

    @OneToMany(mappedBy = "applicant")
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
}
