package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mungfriend.enumeration.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String address;

    @Column(nullable = false)
    private String phoneNum= "";

    @Column(nullable = false)
    private String introduce = "";

    @Column(nullable = false)
    private UserRole userRole = UserRole.USER;

    @Column(nullable = false)
    private boolean isAgree;

//    @Column
//    private String provider;

    @OneToMany(mappedBy = "member")
    private List<Post> myPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Dog> dogList = new ArrayList<>();

    @OneToMany(mappedBy = "applicant")
    private List<Review> applicantReviews = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Review> writerReviews = new ArrayList<>();

    @OneToMany(mappedBy = "applicant")
    private List<Apply> applies = new ArrayList<>();
}
