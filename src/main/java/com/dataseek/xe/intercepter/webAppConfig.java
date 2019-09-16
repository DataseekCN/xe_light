package com.dataseek.xe.intercepter;

import com.dataseek.xe.config.XeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webAppConfig implements WebMvcConfigurer {

    @Autowired
    private XeProperties xeProperties;

    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (xeProperties.isInterTrigger()) {
            registry.addInterceptor(authInterceptor).addPathPatterns("/**");
        }
    }

}
