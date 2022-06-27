package com.project.mungfriend.dto;

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
    private List<Long> dogIdList;
    private LocalDateTime requestStartDate;
    private LocalDateTime requestEndDate;
}
