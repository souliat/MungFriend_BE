package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mungfriend.dto.post.PutPostRequestDto;
import com.project.mungfriend.dto.post.RegisterPostRequestDto;
import com.project.mungfriend.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Post extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private Boolean withMe = false;

    @Column(nullable = false)
    private String dogProfileIds;

    @Column(nullable = false)
    @JsonIgnore
    private Long applyCount = 0L;

    @Column(nullable = false)
    @JsonIgnore
    private Boolean applyByMe = false;

    @Column(nullable = false)
    private Boolean isComplete = false;

    @Column
    private Long matchedApplicantId;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime requestStartDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime requestEndDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Apply> applyList = new ArrayList<>();


    public Post(RegisterPostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.withMe = requestDto.getWithMe();

        StringBuilder dogIds = new StringBuilder();
        for (Long dogId : requestDto.getDogIdList()){
            dogIds.append(dogId);
            dogIds.append(",");
        }
        this.dogProfileIds = dogIds.substring(0, dogIds.length()-1);

        this.requestStartDate = requestDto.getRequestStartDate();
        this.requestEndDate = requestDto.getRequestEndDate();
    }

    public void setMember(Member member){
        this.member = member;
        member.getMyPostList().add(this);
        this.address = member.getAddress();
        this.latitude = member.getLatitude();
        this.longitude = member.getLongitude();
    }

    public void updatePost(PutPostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.withMe = requestDto.getWithMe();

        StringBuilder dogIds = new StringBuilder();
        for (Long dogId : requestDto.getDogIdList()){
            dogIds.append(dogId);
            dogIds.append(",");
        }
        this.dogProfileIds = dogIds.substring(0, dogIds.length()-1);

        this.requestStartDate = requestDto.getRequestStartDate();
        this.requestEndDate = requestDto.getRequestEndDate();
    }
}
