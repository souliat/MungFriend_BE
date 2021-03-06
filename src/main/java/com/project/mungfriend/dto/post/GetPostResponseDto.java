package com.project.mungfriend.dto.post;

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
public class GetPostResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private Boolean withMe;
    private String address;
    private LocalDateTime requestStartDate;
    private LocalDateTime requestEndDate;
    private Long applyCount;
    private Boolean applyByMe;
    private Boolean isComplete;
    private List<String> imagePath = new ArrayList<>();

    private double distance;

    public GetPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.withMe = post.getWithMe();
        this.nickname = post.getMember().getNickname();
        this.address = post.getAddress();
        this.requestStartDate = post.getRequestStartDate();
        this.requestEndDate = post.getRequestEndDate();
        this.applyCount = post.getApplyCount();
        this.applyByMe = post.getApplyByMe();
    }
}
