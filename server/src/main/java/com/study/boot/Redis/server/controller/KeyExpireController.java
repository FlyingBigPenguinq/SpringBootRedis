package com.study.boot.Redis.server.controller;

import com.study.boot.Redis.api.response.BaseResponse;
import com.study.boot.Redis.api.response.StatusCode;
import com.study.boot.Redis.server.enums.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * key的过期失效
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/3/10 10:09
 **/
@RestController
@RequestMapping("key/expire")
public class KeyExpireController extends AbstractController{

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("put")
    public BaseResponse put(@RequestParam String orderNo){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            redisTemplate.opsForValue().set(Constant.RedisExpireKey+orderNo,orderNo,20L, TimeUnit.SECONDS);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @RequestMapping("info")
    public BaseResponse info(@RequestParam String orderNo){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            response.setData(redisTemplate.opsForValue().get(Constant.RedisExpireKey+orderNo));

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    //N分钟请勿重复提交
    @RequestMapping(value = "repeat",method = RequestMethod.POST)
    public BaseResponse repeat(@RequestParam Integer userId,@RequestParam String content){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //TODO:第一次要评论成功，但是并发而来的提交上来的信息要间隔ttl才能再次塞入缓存
            //TODO:api的特性-原子性操作（可以用于充当redis分布式锁的实现）redis+lua/zookeeper/redisson...
            Boolean res=redisTemplate.opsForValue().setIfAbsent(Constant.RedisRepeatKey+userId,content);
            if (res){
                log.info("----提交评论成功----");

                redisTemplate.expire(Constant.RedisRepeatKey+userId,20L,TimeUnit.SECONDS);

                //TODO:后续的操作 ~ 将评论信息塞入db
            }else{
                return new BaseResponse(StatusCode.Fail.getCode(),"您操作过于频繁，请20s后再再次提交评论...");
            }

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}

























