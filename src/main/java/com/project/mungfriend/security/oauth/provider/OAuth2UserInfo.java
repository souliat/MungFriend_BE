package com.project.mungfriend.security.oauth.provider;

public interface OAuth2UserInfo {
    String getProfileImg();
    String getProvider();
    String getEmail();
    String getNickname();

}
