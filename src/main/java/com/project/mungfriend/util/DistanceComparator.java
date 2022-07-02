package com.project.mungfriend.util;

import com.project.mungfriend.dto.post.GetPostResponseDto;

import java.util.Comparator;

// 거리 별 정렬 comparator 정의
public class DistanceComparator implements Comparator<GetPostResponseDto> {
    @Override
    public int compare (GetPostResponseDto a, GetPostResponseDto b){
        // 거리를 비교하여 더 작은 값을 앞으로 정렬
        if(a.getDistance() > b.getDistance()) return 1;
        if(a.getDistance() < b.getDistance()) return -1;
        return 0;
    }
}
