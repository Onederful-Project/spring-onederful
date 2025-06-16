package com.example.onederful.config;

import com.example.onederful.filter.JwtFilter;
import com.example.onederful.security.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtUtil jwtUtil){
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtFilter(jwtUtil));
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
