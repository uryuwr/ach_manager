package com.ch.common.config;

import com.ch.common.intercepotors.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Chen on 2019-02-08-1:35
 */
@Configuration
public class LoginConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        InterceptorRegistration loginRegistry = registry.addInterceptor(loginInterceptor);
        // 拦截路径
        loginRegistry.addPathPatterns("/**");
        // 排除路径
        loginRegistry.excludePathPatterns("/");
        loginRegistry.excludePathPatterns("/v0.1/user/**");
        // 排除资源请求
        loginRegistry.excludePathPatterns("/view/login.html");
        loginRegistry.excludePathPatterns("/view/register.html");
        loginRegistry.excludePathPatterns("/css/*.css");
        loginRegistry.excludePathPatterns("/js/*.js");
        loginRegistry.excludePathPatterns("/image/*.png");
        loginRegistry.excludePathPatterns("/vendor/**");
    }

}
