package org.yangxin.permission;

import com.google.common.collect.Lists;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yangxin.permission.common.HttpInterceptor;
import org.yangxin.permission.filter.LoginFilter;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.servlet.Filter;
import java.util.ArrayList;

@SpringBootApplication
@MapperScan(basePackages = {"org.yangxin.permission.dao"})
public class PermissionApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }

    /**
     * redis
     */
    @Bean
    public ShardedJedisPool shardedJedisPool() {
        JedisShardInfo shardInfo = new JedisShardInfo("127.0.0.1", 6379);
        ArrayList<JedisShardInfo> shards = Lists.newArrayList(shardInfo);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        return new ShardedJedisPool(config, shards);
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
    }

    /**
     * 登录过滤器
     */
    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LoginFilter());
        registration.addUrlPatterns("/sys/*", "/admin/*");
        registration.setOrder(1);
        return registration;
    }
}
