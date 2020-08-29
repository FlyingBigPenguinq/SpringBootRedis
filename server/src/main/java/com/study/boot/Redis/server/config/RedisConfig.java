package com.study.boot.Redis.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName RedisConfig
 * @Description: TODO 自定义Redis的配置类
 * @Author lxl
 * @Date 2020/8/29
 * @Version V1.0
 **/
@Configuration
public class RedisConfig {


    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    public RedisTemplate redisTemplate(){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //设置key/value序列化策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //针对hash数据结构的key而设置
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate(connectionFactory);
        return stringRedisTemplate;
    }


}