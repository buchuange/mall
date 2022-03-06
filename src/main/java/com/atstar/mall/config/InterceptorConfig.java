package com.atstar.mall.config;

import com.atstar.mall.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Dawn
 * @Date: 2022/3/6 21:38
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    // mvc:interceptors
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 要拦截所有访问请求，必须用户登录后才可访问，
        // 但是这样拦截的路径中有一些是不需要用户登录也可访问的
        String[] addPathPatterns = {"/**"};

        // 要排除的路径，排除的路径说明不需要用户登录也可访问
        String[] excludePathPatterns = {"/user/register", "/user/login"};


        // mvc:interceptor
        registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
    }
}
