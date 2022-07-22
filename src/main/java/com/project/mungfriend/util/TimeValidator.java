package com.project.mungfriend.util;

import java.time.LocalDateTime;

public class TimeValidator {

    // 산책 게시글 작성 시 산책 시간, 종료 시간 유효성 검증 모듈화
    public static void validateTime(LocalDateTime reqStartDate, LocalDateTime reqEndDate){

        LocalDateTime now = LocalDateTime.now();

        if (reqStartDate.isBefore(now)
                || reqEndDate.isBefore(now)){
            throw new IllegalArgumentException("산책 시작 시간, 산책 종료 시간은 현재 시간보다 빠를 수 없습니다.");
        }

        if (reqEndDate.isBefore(reqStartDate)) {
            throw new IllegalArgumentException("산책 종료 시간이 산책 시작 시간보다 빠를 수 없습니다.");
        }else if (reqStartDate.isEqual(reqEndDate)){
            throw new IllegalArgumentException("산책 시작 시간, 산책 종료 시간은 같을 수 없습니다.");
        }

    }
}
