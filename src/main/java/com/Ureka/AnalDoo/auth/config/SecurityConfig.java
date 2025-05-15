package com.Ureka.AnalDoo.auth.config;

import com.Ureka.AnalDoo.auth.filter.JwtAuthenticationFilter;
import com.Ureka.AnalDoo.auth.handler.JwtAccessDeniedHandler;
import com.Ureka.AnalDoo.auth.handler.JwtAuthenticationEntryPointHandler;
import com.Ureka.AnalDoo.auth.jwt.JWTFilter;
import com.Ureka.AnalDoo.auth.jwt.JWTUtil;
import com.Ureka.AnalDoo.auth.filter.LoginFilter;
import com.Ureka.AnalDoo.auth.service.CustomUserDetailsService;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserRepository userRepository, CustomUserDetailsService customUserDetailsService) throws Exception {

        //cors 설정
        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {


                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(List.of(
                                        "http://localhost:3000",
                                        "https://anal-doo-fe.vercel.app"
                                ));
                                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                                configuration.setAllowedHeaders(List.of("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setMaxAge(3600L);

                                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                                source.registerCorsConfiguration("/**", configuration);
                                return configuration;
                            }
                        })
                );

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        http
                .exceptionHandling((exception)
                        -> exception.authenticationEntryPoint(authenticationEntryPointHandler));
        http
                .exceptionHandling((exception)
                        -> exception.accessDeniedHandler(accessDeniedHandler));

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/users/login",
                                "/api/v1/users/signup",
                                "/api/v1/users/reissue",
                                "/api/v1/users/join",
                                "/api/v1/competitions/list",
                                "/").permitAll()
                        .anyRequest().authenticated()
                );
        //JwtAuthenticationFilter 등록
        http
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtUtil, customUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                );

        //필터 추가 LoginFilter()는 인자를 받음
        // (AuthenticationManger() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(
                        new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}
