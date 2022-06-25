package com.project.mungfriend.enumeration;

public enum UserRole {

    ADMIN(Authority.ADMIN),
    USER(Authority.USER);

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
