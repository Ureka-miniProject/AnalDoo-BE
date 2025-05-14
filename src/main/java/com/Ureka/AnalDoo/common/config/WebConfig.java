package com.Ureka.AnalDoo.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**") // cors를 적용할 spring서버의 url 패턴.
                .allowedOrigins(
                        "http://localhost:3000","https://anal-doo-fe.vercel.app","http://anal-doo-fe.vercel.app") // cors를 허용할 도메인. 제한을 모두 해제하려면 "**"
                .allowedMethods("*") // cors를 허용할 method + DELETE 추가
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true);
    }
}
