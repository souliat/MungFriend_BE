package com.project.mungfriend.security;

import com.project.mungfriend.enumeration.UserRole;
import com.project.mungfriend.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Transactional
public class UserDetailsImpl implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attributes;

    //일반로그인
    public UserDetailsImpl(Member member) {
        this.member = member;
    }


    //OAuth 로그인
    public UserDetailsImpl(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }


    public Member getMember() {
        return member;
    }

//    public List<Post> getPosts() {
//        return member.getPosts();
//    }

    public String getNickname() {return member.getNickname();}

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = member.getUserRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
