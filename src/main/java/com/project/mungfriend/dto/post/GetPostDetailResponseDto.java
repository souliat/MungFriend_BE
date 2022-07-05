package com.project.mungfriend.dto.post;

import com.project.mungfriend.model.Apply;
import com.project.mungfriend.model.Dog;
import com.project.mungfriend.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetPostDetailResponseDto {
    private Long id;
    private String nickname;

    private String dogProfileImgUrl;
    private String title;
    private String content;
    private String address;
    private LocalDateTime requestStartDate;
    private LocalDateTime requestEndDate;
    private Long applyCount;
    private Boolean applyByMe;
    private Boolean isComplete;

    private Long matchedApplicantId;

    private double distance;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<Apply> applyList = new ArrayList<>();
    private List<Dog> dogList = new ArrayList<>();

    public GetPostDetailResponseDto(Post post) {
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.address = post.getAddress();
        this.requestStartDate = post.getRequestStartDate();
        this.requestEndDate = post.getRequestEndDate();
        this.applyCount = post.getApplyCount();
        this.applyByMe = post.getApplyByMe();
        this.isComplete = post.getIsComplete();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.matchedApplicantId = post.getMatchedApplicantId();
    }
}
