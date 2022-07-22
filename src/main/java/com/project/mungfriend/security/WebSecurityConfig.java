package com.project.mungfriend.security;

import com.project.mungfriend.enumeration.UserRole;
import com.project.mungfriend.security.jwt.JwtAccessDeniedHandler;
import com.project.mungfriend.security.jwt.JwtAuthenticationEntryPoint;
import com.project.mungfriend.security.jwt.JwtSecurityConfig;
import com.project.mungfriend.security.jwt.TokenProvider;
import com.project.mungfriend.security.oauth.provider.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity //스프링 시큐리티가 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final OAuth2SuccessHandler successHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private OAuthUserDetailsServiceImpl oAuthUserDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource()); //cors 활성화
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/member/**").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/chatting/**").permitAll()

                // 산책 전체 조회 페이지는 필수 값을 입력하지 않은 사용자도 접근 가능하다.
                .antMatchers(HttpMethod.GET,"/api/posts")
                .hasAnyAuthority(UserRole.Authority.USER, UserRole.Authority.QUALIFIED_USER)

                // 그 외 모든 API는 필수 값을 입력해야만 사용 가능하다.
                .anyRequest().hasAuthority(UserRole.Authority.QUALIFIED_USER)

                .and()
                .apply(new JwtSecurityConfig(tokenProvider))

                .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(successHandler)
                .userInfoEndpoint()
                .userService(oAuthUserDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedOriginPattern("http://localhost:3000"); // 배포 전 모두 허용
        // 프론트 서버 (백엔드 테스트 용) : http://mung-friend-fe.s3-website.ap-northeast-2.amazonaws.com
        // 프론트 서버 : https://d3n0oswt21uayp.cloudfront.net
        // 프론트 HTTPS 서버 : https://mungfriend.com
        // 프론트 로컬 : http://localhost:3000
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
