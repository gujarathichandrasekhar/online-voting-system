package com.nt.Designpattren.Designpattren.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig
        implements WebMvcConfigurer {

    private final ResultAccessInterceptor
            resultAccessInterceptor;

    public WebMvcConfig(
            ResultAccessInterceptor resultAccessInterceptor) {

        this.resultAccessInterceptor =
                resultAccessInterceptor;
    }

    @Override
    public void addInterceptors(
            InterceptorRegistry registry) {

        registry.addInterceptor(
                resultAccessInterceptor
        ).addPathPatterns(
                "/report",
                "/winner"
        );
    }
}