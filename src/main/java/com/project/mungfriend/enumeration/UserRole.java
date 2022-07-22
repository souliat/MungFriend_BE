package com.project.mungfriend.enumeration;

public enum UserRole {

    ADMIN(Authority.ADMIN), // 필수 입력 값을 모두 입력한 정상 유저
    USER(Authority.USER); // 필수 입력 값이 누락된 비정상 유저

    private final String authority;

    UserRole(String authority){
        this.authority = authority;
    }

    public String getAuthority(){
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

}
