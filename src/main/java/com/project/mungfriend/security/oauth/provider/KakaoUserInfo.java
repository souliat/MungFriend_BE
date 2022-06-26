package com.project.mungfriend.security.oauth.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public String getProfileImg(){
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
        return (String) profile.get("thumbnail_image_url");
    }
    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getNickname() {
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
        return (String) profile.get("nickname");
    }
}
