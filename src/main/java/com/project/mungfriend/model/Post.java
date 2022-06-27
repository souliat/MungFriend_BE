package com.project.mungfriend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.mungfriend.dto.RegisterPostRequestDto;
import com.project.mungfriend.enumeration.Category;
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
    private String dogProfileIds;

    @Column(nullable = false)
    private Long applyCount = 0L;

    @Column(nullable = false)
    private Boolean applyByMe = false;

    @Column(nullable = false)
    private Boolean isComplete = false;

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

    @OneToMany(mappedBy = "post")
    private List<Apply> applies = new ArrayList<>();


    public Post(RegisterPostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();

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
    }
}
