package com.project.mungfriend.util;

import org.springframework.stereotype.Component;

@Component
public class DistanceCalculator {

    /**
     * 두 지점간의 거리 계산
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     */
    public double calcDistance(double lat1, double lon1, double lat2, double lon2, String unit){
        double theta = lon1 - lon2;
        double dist = Math.sin(degToRad(lat1)) * Math.sin(degToRad(lat2))
                + Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * Math.cos(degToRad(theta));

        dist = Math.acos(dist);
        dist = radToDeg(dist);
        dist = dist * 60 * 1.1515;

        if(unit.equals("kilometer")){
            dist = dist * 1.609344;
        }else if(unit.equals("meter")){
            dist = dist * 1609.344;
        }

        return (dist);
    }

    // 각도를 라디안 값으로 변환
    public static double degToRad(double deg){
        return (deg * Math.PI / 180.0);
    }

    // 라디안 값을 각도로 변환
    public static double radToDeg(double rad){
        return (rad * 180 / Math.PI);
    }


}
