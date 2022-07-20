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
        this.nickname = post.getMember().getNickname();
        this.address = post.getAddress();
        this.requestStartDate = post.getRequestStartDate();
        this.requestEndDate = post.getRequestEndDate();
        this.applyCount = post.getApplyCount();
        this.applyByMe = post.getApplyByMe();

        // 게시글 전체 조회, 거리순 조회 시 현재 시간을 기준으로 요청 시간이 지났으면 모집 종료로 바꿔주기
        if ( post.getRequestStartDate().isAfter(LocalDateTime.now()) ){
            post.setIsComplete(true);
        }
        this.isComplete = post.getIsComplete();

    }
}
