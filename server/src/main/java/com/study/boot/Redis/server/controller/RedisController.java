package com.study.boot.Redis.server.controller;

import com.study.boot.Redis.api.response.BaseResponse;
import com.study.boot.Redis.api.response.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName RedisController
 * @Description: TODO
 * @Author lxl
 * @Date 2020/8/29
 * @Version V1.0
 **/
@Controller
@RequestMapping("base")
public class RedisController extends AbstractController{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("hello")
    @ResponseBody
    public BaseResponse helloWorld(){
        return new BaseResponse(StatusCode.Success);
    }


    @RequestMapping(value = "string/data",method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse stringData(String name){
        System.out.println("进来了" + name);
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try{
            stringRedisTemplate.opsForValue().set("myname",name);
        }catch (Exception e){
            baseResponse = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return baseResponse;
    }
}
