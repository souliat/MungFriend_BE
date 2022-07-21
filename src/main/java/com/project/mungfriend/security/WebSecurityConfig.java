package com.project.mungfriend.security;

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
                .antMatchers("/chatting/**").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
//                .antMatchers(HttpMethod.GET,"/api/posts").permitAll()
//                .antMatchers(HttpMethod.GET,"/api/ranks").permitAll()
//                .antMatchers("/auth/**", "/oauth2/**").permitAll()
                .anyRequest().authenticated()

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
