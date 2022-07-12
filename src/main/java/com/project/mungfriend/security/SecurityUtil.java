package com.project.mungfriend.security;

import com.project.mungfriend.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() { }

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static String getCurrentMemberUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new NullPointerException("Security Context에 인증 정보가 없습니다.");
        }
        //authenticaion은 principal을 extends 받은 객체. getName() 메서드는 사용자의 이름을 넘겨주었다.
        //String type의 username (유저의 id)
        return authentication.getName();
    }

    // JQPL로 멤버 객체 조회하여 UserDetailsImpl이 Authentication에 저장되었다면 아래와 같은 방식으로 Member객체 리턴 가능
    public static Member getCurrentMember() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new NullPointerException("Security Context 에 인증 정보가 없습니다.");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getMember();
    }
}
