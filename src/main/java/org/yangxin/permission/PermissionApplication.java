package org.yangxin.permission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yangxin.permission.common.HttpInterceptor;

@SpringBootApplication
public class PermissionApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
    }
}
