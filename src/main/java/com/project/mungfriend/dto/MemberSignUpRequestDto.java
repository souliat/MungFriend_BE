package com.project.mungfriend.dto;

import com.project.mungfriend.enumeration.UserRole;
import com.project.mungfriend.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
public class MemberSignUpRequestDto {

    private String username;
    private String password;
    private String nickname;
    private String email;
    private String address;
    private Boolean isAgree;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .userRole(UserRole.USER)
                .nickname(nickname)
                .email(email)
                .address(address)
                .isAgree(isAgree)
                .build();
    }
}
