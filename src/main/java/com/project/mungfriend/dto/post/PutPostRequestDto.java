package com.project.mungfriend.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PutPostRequestDto {
    private String title;
    private String content;
    private Boolean withMe;
    private List<Long> dogIdList;
    private LocalDateTime requestStartDate;
    private LocalDateTime requestEndDate;
}
