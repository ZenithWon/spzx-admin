package com.zenith.spzx.manager.config;

import com.zenith.spzx.manager.interceptor.LoginAuthInterceptor;
import com.zenith.spzx.manager.properties.AuthProperties;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginAuthInterceptor loginAuthInterceptor;

    @Autowired
    private AuthProperties authProperties;

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginAuthInterceptor).excludePathPatterns(authProperties.getWhiteUrl()).addPathPatterns("/**");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
