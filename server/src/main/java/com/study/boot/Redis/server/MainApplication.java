package com.study.boot.Redis.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName MainApplication
 * @Description: TODO
 * @Author lxl
 * @Date 2020/8/28
 * @Version V1.0
 **/
@SpringBootApplication
@ImportResource("classpath:/spring/spring-jdbc.xml")
@MapperScan("com.study.boot.redis.model.mapper")
@EnableScheduling
@EnableCaching
@EnableAsync
public class MainApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }

    public static void main(String[] args){
        SpringApplication.run(MainApplication.class, args);
    }
}
