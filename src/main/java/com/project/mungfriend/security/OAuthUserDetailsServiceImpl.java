package com.project.mungfriend.security;

import com.project.mungfriend.enumeration.MailType;
import com.project.mungfriend.enumeration.UserRole;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.security.oauth.provider.GoogleUserInfo;
import com.project.mungfriend.security.oauth.provider.KakaoUserInfo;
import com.project.mungfriend.security.oauth.provider.OAuth2UserInfo;
import com.project.mungfriend.util.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthUserDetailsServiceImpl extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private static String socialPassword;
    @Value("${spring.mail.password}")
    public void setPassword(String value){
        socialPassword = value;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);


        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            System.out.println(oAuth2User.getAttributes());
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            System.out.println("카카오 로그인 요청");
            System.out.println(oAuth2User.getAttributes());
            oAuth2UserInfo = new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"));
        }
        else{
            System.out.println("구글과 카카오톡 로그인만 가능합니다.");
        }

        String provider = oAuth2UserInfo.getProvider(); //google,kakao
        String email = oAuth2UserInfo.getEmail();
        System.out.println("카카오, 구글에서 가져온 email = " + email);
        String[] emailUsername = email.split("@");
        String username = provider + "_" + emailUsername[0];// google_19146317978241904 / facebook_123142352452
        String password = passwordEncoder.encode(socialPassword);
        UserRole userRole = UserRole.USER;
        
        // 카카오, 구글의 닉네임이 같을 경우 unique 속성이 중복된다는 에러 발생 -> suffix로 provider 넣어줌
        String nickname = oAuth2UserInfo.getNickname() + "_" + provider;

//        String profileImg = oAuth2UserInfo.getProfileImg();

        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            // 첫번 째 로그인 -> 소셜 회원가입 상황
            member = new Member(username, email, password, nickname, userRole, provider);
            MailSender.sendMail(member.getEmail(), member.getNickname() + "님, 회원가입을 축하드립니다!",
                    member.getNickname(), MailType.SOCIAL_SIGNUP);
        }

        memberRepository.save(member);

//        Authentication auth = new UsernamePasswordAuthenticationToken(member, null);
//        SecurityContextHolder.getContext().setAuthentication(auth);

        return new UserDetailsImpl(member, oAuth2User.getAttributes());
    }
}
