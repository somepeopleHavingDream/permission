package org.yangxin.permission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yangxin.permission.common.HttpInterceptor;
import org.yangxin.permission.filter.LoginFilter;

import javax.servlet.Filter;

@SpringBootApplication
@MapperScan(basePackages = {"org.yangxin.permission.dao"})
public class PermissionApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LoginFilter());
        registration.addUrlPatterns("/sys/*", "/admin/*");
        registration.setOrder(1);
        return registration;
    }
}
